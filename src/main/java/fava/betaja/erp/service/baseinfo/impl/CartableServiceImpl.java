package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import fava.betaja.erp.entities.da.BlockValue;
import fava.betaja.erp.entities.security.Role;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.enums.baseinfo.CartableState;
import fava.betaja.erp.enums.da.BlockValueState;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.baseinfo.CartableDtoMapper;
import fava.betaja.erp.repository.baseinfo.CartableRepository;
import fava.betaja.erp.repository.baseinfo.FlowRuleStepRepository;
import fava.betaja.erp.repository.da.BlockValueRepository;
import fava.betaja.erp.repository.security.UserRoleRepository;
import fava.betaja.erp.service.baseinfo.CartableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final FlowRuleStepRepository flowRuleStepRepository;
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

    public CartableDto approveCartable(UUID cartableId, String comment) {
        Cartable cartable = cartableRepository.findById(cartableId)
                .orElseThrow(() -> new ServiceException("کارتابل یافت نشد"));

        // ثبت توضیحات کاربر در description
        if (comment != null && !comment.isBlank()) {
            cartable.setDescription(comment);
        }

        // ثبت تاریخ اقدام و کاربر در CartableHistory
        CartableHistory history = new CartableHistory();
        history.setCartable(cartable);
        history.setUser(currentUser);
        history.setActionType(ActionTypeEnum.APPROVE);
        history.setComment(comment);
        history.setActionDate(new Date());
        cartableHistoryRepository.save(history);

        // بررسی مرحله بعدی
        FlowRuleStep nextStep = cartable.getNextStep();
        if (nextStep != null) {
            // پیدا کردن دریافت‌کننده جدید
            Users nextRecipient = userExtraRepository.findFirstByRoleIdAndOrganizationUnitId(
                    nextStep.getRole().getId(),
                    cartable.getFlowRuleDomain().getFlowRule().getOrganizationUnit().getId()
            );

            if (nextRecipient == null) {
                throw new ServiceException("کاربری با نقش و یگان مشخص شده برای مرحله بعدی یافت نشد.");
            }

            cartable.setRecipient(nextRecipient);
            cartable.setCurrentStep(nextStep);
            cartable.setNextStep(nextStep.getNextSteps().isEmpty() ? null : nextStep.getNextSteps().get(0));
            cartable.setState(CartableState.IN_PROGRESS);
        } else {
            // اگر مرحله بعدی وجود نداشت، کارتابل تمام شده
            cartable.setState(CartableState.COMPLETED);
            cartable.setRecipient(null);
        }

        return cartableRepository.save(cartable);
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
