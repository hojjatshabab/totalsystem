package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.entities.baseinfo.CartableHistory;
import fava.betaja.erp.entities.baseinfo.FlowRule;
import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import fava.betaja.erp.entities.da.BlockValue;
import fava.betaja.erp.entities.security.Role;
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

import java.time.LocalDate;
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

    @Override
    public CartableDto nextStepCartable(CartableDto dto) {
        validate(dto, false);
        dto.setSenderId(dto.getRecipientId());
        dto.setSendDate(LocalDate.now());
        dto.setState(CartableState.IN_PROGRESS);

        FlowRuleStep flowRuleStep = flowRuleStepRepository.findById(dto.getCurrentStepId()).get();
        FlowRuleStep nextStep = flowRuleStep.getNextSteps().get(0);
        Role role = nextStep.getRole();
        Users recipientUser = userRoleRepository.findByRoleId(role.getId()).stream().findFirst().get().getUser();

        dto.setRecipientId(recipientUser.getId());
        dto.setCurrentStepId(flowRuleStep.getNextSteps().get(0).getId());

        Cartable entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CartableDto cartableToNextStep(UUID cartableId, String comment) {
        // 1. واکشی کارتابل
        Cartable cartable = repository.findById(cartableId)
                .orElseThrow(() -> new ServiceException("Cartable not found: " + cartableId));

        // 2. کاربر فعلی
        Users currentUser = usersDtoMapper.toEntity(usersService.getCurrentUser());

        // 3. ثبت تاریخچه اقدام
        CartableHistory history = new CartableHistory();
        history.setCartable(cartable);
        history.setUser(currentUser);
        history.setActionType(ActionTypeEnum.APPROVE);
        history.setComment(comment);
        history.setActionDate(new Date());
        cartableHistoryRepository.save(history);

        // 4. گام جاری را از کارتابل یا جریان پیدا کن
        FlowRule flowRule = cartable.getFlowRuleDomain().getFlowRule();
        FlowRuleStep currentStep = cartable.getCurrentStep();

        if (currentStep == null) {
            // اگر جریان تازه شروع شده
            currentStep = flowRuleStepRepository.findFirstByFlowRuleIdOrderByStepOrder(flowRule.getId())
                    .orElseThrow(() -> new ServiceException("No steps defined for flow: " + flowRule.getName()));
            cartable.setCurrentStep(currentStep);
        }

        // 5. بررسی آیا مرحله فعلی، آخرین مرحله است؟
        if (Boolean.TRUE.equals(currentStep.getFinalStep())) {
            // پایان جریان
            cartable.setState(CartableState.COMPLETED);
            cartable.setRecipient(null);
            cartable.setNextStep(null);
            if (comment != null && !comment.isEmpty()) {
                cartable.setDescription(appendDescription(cartable.getDescription(), currentUser.getUsername(), comment));
            } else {
                cartable.setDescription(null);
            }
            repository.save(cartable);
            return mapper.toDto(cartable);
        }

        // 6. مرحله بعدی را بر اساس stepOrder پیدا کن
        FlowRuleStep nextStep = flowRuleStepRepository
                .findFirstByFlowRuleIdAndStepOrderGreaterThanOrderByStepOrderAsc(
                        flowRule.getId(), currentStep.getStepOrder())
                .orElseThrow(() -> new ServiceException("Next step not found after step: "));

        // 7. پیدا کردن گیرنده مرحله بعدی
        Long orgUnitId = cartable.getSender() != null && cartable.getSender().getOrganizationUnit() != null
                ? cartable.getSender().getOrganizationUnit().getId()
                : null;

        Users recipient = userRepository.findFirstByRoleIdAndOrganizationUnitId(
                nextStep.getRole().getId(), orgUnitId);

        if (recipient == null) {
            throw new ServiceException("No recipient found for role=" + nextStep.getRole().getName());
        }

        // 8. بروزرسانی کارتابل
        cartable.setCurrentStep(nextStep);
        cartable.setRecipient(recipient);
        cartable.setState(CartableState.IN_PROGRESS);
        cartable.setDescription(appendDescription(cartable.getDescription(), currentUser.getUsername(), comment));

        // 9. اگر مرحله بعدی finalStep دارد، nextStep=null بگذار
        cartable.setNextStep(Boolean.TRUE.equals(nextStep.getFinalStep()) ? null :
                flowRuleStepRepository
                        .findFirstByFlowRuleIdAndStepOrderGreaterThanOrderByStepOrderAsc(
                                flowRule.getId(), nextStep.getStepOrder())
                        .orElse(null));

        // 10. ذخیره و بازگشت
        repository.save(cartable);
        return mapper.toDto(cartable);
    }

    private String appendDescription(String existing, String username, String comment) {
        if (comment == null || comment.isBlank()) return existing;
        String prefix = existing == null ? "" : existing + "\n";
        return prefix + "[" + username + "] " + comment;
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
