package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.entities.baseinfo.CartableHistory;
import fava.betaja.erp.entities.baseinfo.FlowRule;
import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import fava.betaja.erp.entities.da.BlockValue;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.enums.baseinfo.ActionTypeEnum;
import fava.betaja.erp.enums.baseinfo.CartableState;
import fava.betaja.erp.enums.da.BlockValueState;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.baseinfo.CartableDtoMapper;
import fava.betaja.erp.mapper.security.UsersDtoMapper;
import fava.betaja.erp.repository.baseinfo.CartableHistoryRepository;
import fava.betaja.erp.repository.baseinfo.CartableRepository;
import fava.betaja.erp.repository.baseinfo.FlowRuleStepRepository;
import fava.betaja.erp.repository.da.BlockValueRepository;
import fava.betaja.erp.repository.security.UserRepository;
import fava.betaja.erp.repository.security.UserRoleRepository;
import fava.betaja.erp.service.baseinfo.CartableService;
import fava.betaja.erp.service.security.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartableServiceImpl implements CartableService {

    private final CartableRepository repository;
    private final UserRepository userRepository;
    private final FlowRuleStepRepository flowRuleStepRepository;
    private final UsersService usersService;
    private final UsersDtoMapper usersDtoMapper;
    private final CartableHistoryRepository cartableHistoryRepository;
    private final BlockValueRepository blockValueRepository;
    private final UserRoleRepository userRoleRepository;
    private final CartableDtoMapper mapper;

    @Override
    public CartableDto save(CartableDto dto) {
        validate(dto, true);
        log.info("Saving Cartable: documentNumber={}", dto.getDocumentNumber());
        Cartable entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CartableDto update(CartableDto dto) {
        validate(dto, false);
        log.info("Updating Cartable: id={}, documentNumber={}", dto.getId(), dto.getDocumentNumber());
        Cartable entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CartableDto acceptCartable(CartableDto dto) {
        validate(dto, false);

        dto.setState(CartableState.APPROVED);

        BlockValue blockValue = blockValueRepository.findById(dto.getDocumentId()).get();
        blockValue.setBlockValueState(BlockValueState.APPROVED);
        blockValueRepository.save(blockValue);


        Cartable entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    @Override
    public CartableDto cartableToNextStep(UUID cartableId, String comment) {
        Cartable cartable = repository.findById(cartableId)
                .orElseThrow(() -> new ServiceException("  کارتابل یافت نشد.  "));

        if (cartable.getState().equals(CartableState.APPROVED)) {
            throw new ServiceException(" کارتابل قبلاً تایید نهایی شده است. ");
        }

        Users currentUser = usersDtoMapper.toEntity(usersService.getCurrentUser());

        validateUserCanProceedForNextStep(cartable, currentUser);

        CartableHistory history = new CartableHistory();
        history.setCartable(cartable);
        history.setUser(currentUser);
        history.setActionType(ActionTypeEnum.APPROVE);
        history.setComment(comment);
        history.setActionDate(new Date());
        cartableHistoryRepository.save(history);

        FlowRule flowRule = cartable.getFlowRuleDomain().getFlowRule();
        FlowRuleStep currentStep = cartable.getCurrentStep();

        if (currentStep == null) {
            currentStep = flowRuleStepRepository.findFirstByFlowRuleIdOrderByStepOrder(flowRule.getId())
                    .orElseThrow(() -> new ServiceException("No steps defined for flow: " + flowRule.getName()));
            cartable.setCurrentStep(currentStep);
        }

        if (Boolean.TRUE.equals(currentStep.getFinalStep())) {
            cartable.setState(CartableState.APPROVED);
            cartable.setNextStep(null);
            if (comment != null && !comment.isEmpty()) {
                cartable.setDescription(comment);
            } else {
                cartable.setDescription(null);
            }
            repository.save(cartable);
            return mapper.toDto(cartable);
        }

        FlowRuleStep nextStep = flowRuleStepRepository
                .findFirstByFlowRuleIdAndStepOrderGreaterThanOrderByStepOrderAsc(
                        flowRule.getId(), currentStep.getStepOrder())
                .orElseThrow(() -> new ServiceException(" اطلاعات مرحله بعد یافت نشد "));
        Long orgUnitId;

        if (nextStep.getOrganizationUnit() == null) {
            orgUnitId = cartable.getSender() != null && cartable.getSender().getOrganizationUnit() != null
                    ? cartable.getSender().getOrganizationUnit().getId()
                    : null;
        } else {
            orgUnitId = nextStep.getOrganizationUnit().getId();
        }

        Users recipient = userRepository.findFirstByRoleIdAndOrganizationUnitId(
                nextStep.getRole().getId(), orgUnitId);

        if (recipient == null) {
            throw new ServiceException("هیچ گیرنده ای یافت نشد با نقش =" + nextStep.getRole().getTitle());
        }

        cartable.setCurrentStep(nextStep);
        cartable.setSender(currentUser);
        cartable.setRecipient(recipient);
        cartable.setState(CartableState.IN_PROGRESS);
        cartable.setDescription(comment);

        cartable.setNextStep(Boolean.TRUE.equals(nextStep.getFinalStep()) ? null :
                flowRuleStepRepository
                        .findFirstByFlowRuleIdAndStepOrderGreaterThanOrderByStepOrderAsc(
                                flowRule.getId(), nextStep.getStepOrder())
                        .orElse(null));

        repository.save(cartable);
        return mapper.toDto(cartable);
    }


    private void validateUserCanProceedForNextStep(Cartable cartable, Users currentUser) {
        FlowRuleStep currentStep = cartable.getCurrentStep();
        if (currentStep == null) {
            throw new ServiceException("Current step is not defined for this cartable.");
        }
        boolean validRole;
        boolean validOrgUnit;
        if (cartable.getState().equals(CartableState.IN_PROGRESS)) {
            validRole = userRoleRepository.findByUserId(currentUser.getId()).stream().map(userRole -> userRole.getRole().getId())
                    .anyMatch(ur -> ur == currentStep.getRole().getId());

            validOrgUnit = currentUser.getOrganizationUnit() != null
                    && cartable.getRecipient().getOrganizationUnit() != null
                    && currentUser.getOrganizationUnit().getId().equals(cartable.getRecipient().getOrganizationUnit().getId());
        } else if (cartable.getState().equals(CartableState.RETURNED)) {
            validRole = (currentUser.getId() == cartable.getRecipient().getId());
            validOrgUnit = validRole;
        } else {
            throw new ServiceException(" کارتابل قبلاً تایید نهایی شده است. ");
        }

        if (!validRole || !validOrgUnit) {
            throw new ServiceException("شما مجاز به انجام اقدام در این مرحله نیستید.");
        }
    }

    @Transactional
    @Override
    public CartableDto returnCartableToPreviousStep(UUID cartableId, String comment) {
        Cartable cartable = repository.findById(cartableId)
                .orElseThrow(() -> new ServiceException("کارتابل یافت نشد."));

        if (cartable.getState().equals(CartableState.APPROVED)) {
            throw new ServiceException(" کارتابل قبلاً تایید نهایی شده است. ");
        }

        Users currentUser = usersDtoMapper.toEntity(usersService.getCurrentUser());

        validateUserCanProceedForPreviousStep(cartable, currentUser);


        CartableHistory history = new CartableHistory();
        history.setCartable(cartable);
        history.setUser(currentUser);
        history.setActionType(ActionTypeEnum.REJECT);
        history.setComment(comment);
        history.setActionDate(new Date());
        cartableHistoryRepository.save(history);

        Users previousSender = cartable.getSender();
        if (previousSender == null) {
            throw new ServiceException("فرستنده قبلی برای بازگشت کارتابل یافت نشد.");
        }

        FlowRuleStep currentStep = cartable.getCurrentStep();

        FlowRuleStep previousStep = cartable.getCurrentStep().getPreviousStep();

        Long orgUnitId;
        if (previousStep.getOrganizationUnit() == null) {
            orgUnitId = cartableHistoryRepository
                    .findByCartableIdOrderByCreationDateTimeAsc(cartableId)
                    .get(0).getUser().getOrganizationUnit().getId();
        } else {
            orgUnitId = previousStep.getOrganizationUnit().getId();
        }

        Users recipient = userRepository.findFirstByRoleIdAndOrganizationUnitId(
                previousStep.getRole().getId(), orgUnitId);

        if (recipient == null) {
            throw new ServiceException("گیرنده مرحله قبلی یافت نشد با نقش: " + previousStep.getRole().getTitle());
        }

        cartable.setCurrentStep(previousStep);
        cartable.setRecipient(recipient);
        cartable.setSender(currentUser);
        cartable.setState(CartableState.RETURNED);

        cartable.setDescription(comment);

        cartable.setNextStep(currentStep);

        repository.save(cartable);
        return mapper.toDto(cartable);
    }

    private void validateUserCanProceedForPreviousStep(Cartable cartable, Users currentUser) {
        FlowRuleStep currentStep = cartable.getCurrentStep();
        if (currentStep == null) {
            throw new ServiceException("Current step is not defined for this cartable.");
        }

        boolean validRole;
        boolean validOrgUnit;
        if (cartable.getState().equals(CartableState.IN_PROGRESS)) {
            validRole = userRoleRepository.findByUserId(currentUser.getId()).stream().map(userRole -> userRole.getRole().getId())
                    .anyMatch(ur -> ur == currentStep.getRole().getId());

            validOrgUnit = currentUser.getOrganizationUnit() != null
                    && cartable.getRecipient().getOrganizationUnit() != null
                    && currentUser.getOrganizationUnit().getId().equals(cartable.getRecipient().getOrganizationUnit().getId());
        } else if (cartable.getState().equals(CartableState.RETURNED)) {
            validRole = (currentUser.getId() == cartable.getRecipient().getId()) && currentStep.getStepOrder() != 0;
            validOrgUnit = validRole;
        } else {
            throw new ServiceException(" کارتابل قبلاً تایید نهایی شده است. ");
        }

        if (!validRole || !validOrgUnit) {
            throw new ServiceException("شما مجاز به انجام اقدام در این مرحله نیستید.");
        }
    }

    @Override
    public PageResponse<CartableDto> findAll(PageRequest<CartableDto> model) {
        List<CartableDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<CartableDto> findByStatePage(CartableState state, PageRequest<CartableDto> model) {
        List<CartableDto> result = repository
                .findByState(state, Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<CartableDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartableDto> findByRecipientIdOrderBySendDateDesc(Long recipientId) {
        return repository.findByRecipientIdOrderBySendDateDesc(recipientId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartableDto> findByCurrentStepId(UUID currentStepId) {
        return repository.findByCurrentStepId(currentStepId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartableDto> findByStatePage(String state) {
        return repository.findByState(state)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartableDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        Cartable cartable = repository.findById(id)
                .orElseThrow(() -> new ServiceException("Cartable با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted Cartable: id={}, documentNumber={}", cartable.getId(), cartable.getDocumentNumber());
    }

    private void validate(CartableDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("Cartable برای بروزرسانی موجود نیست.");
        }
        if (dto.getDocumentId() == null) {
            throw new ServiceException("DocumentId الزامی است.");
        }
    }
}
