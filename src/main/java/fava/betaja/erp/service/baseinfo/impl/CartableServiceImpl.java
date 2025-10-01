package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.entities.baseinfo.FlowRule;
import fava.betaja.erp.entities.baseinfo.FlowRuleDomain;
import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import fava.betaja.erp.entities.da.BlockValue;
import fava.betaja.erp.entities.security.Role;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.enums.baseinfo.CartableState;
import fava.betaja.erp.enums.baseinfo.Priority;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.baseinfo.CartableDtoMapper;
import fava.betaja.erp.repository.baseinfo.CartableRepository;
import fava.betaja.erp.repository.baseinfo.FlowRuleDomainRepository;
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
    private final FlowRuleDomainRepository flowRuleDomainRepository;
    private final FlowRuleStepRepository flowRuleStepRepository;
    private final BlockValueRepository blockValueRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UsersService usersService;
    private final CartableDtoMapper mapper;

    @Override
    public CartableDto save(CartableDto dto) {
        validate(dto, true);

        if (dto.getDocumentId() == null) {
            throw new ServiceException("documentId is empty");
        }
        BlockValue blockValue = Optional.ofNullable(dto.getDocumentId())
                .flatMap(blockValueRepository::findById).get();

        if (dto.getFlowRuleDomainEntityName() == null || dto.getFlowRuleDomainEntityName() == "") {
            throw new ServiceException("entity name is empty");
        }
        Optional<FlowRuleDomain> optionalFlowRule = flowRuleDomainRepository
                .findByEntityNameIgnoreCase(dto.getFlowRuleDomainEntityName())
                .stream().findFirst();
        if (!optionalFlowRule.isPresent()) {
            throw new ServiceException("Flow rule domain not found");
        }

        FlowRule flowRule = optionalFlowRule.get().getFlowRule();

        List<FlowRuleStep> flowRuleSteplist = flowRuleStepRepository
                .findByFlowRuleIdOrderByStepOrderAsc(flowRule.getId());

        FlowRuleStep flowRuleStep = flowRuleSteplist.stream().findFirst().get();

        dto.setCurrentStepId(flowRuleStep.getId());
        dto.setNextStepId(flowRuleSteplist.get(1).getId());

        Role role = flowRuleStep.getRole();

        Users recipientUser = userRoleRepository.findByRoleId(role.getId()).stream().findFirst().get().getUser();
        Users senderUser = usersService.getCurrentUser();

        dto.setSenderId(senderUser.getId());
        dto.setRecipientId(recipientUser.getId());
        dto.setFlowRuleDomainId(optionalFlowRule.get().getId());

        StringBuilder title = new StringBuilder();
        title.append(flowRule.getName())
                .append("، ")
                .append(blockValue.getProjectPeriod().getPeriodRange().getName())
                .append(" - ")
                .append(blockValue.getProjectPeriod().getYear());
        dto.setTitle(title.toString());
        dto.setState(CartableState.PENDING);
        dto.setSendDate(LocalDate.now());
        dto.setPriority(Priority.NORMAL);
        dto.setRead(false);

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
    public PageResponse<CartableDto> findAll(PageRequest<CartableDto> model) {
        List<CartableDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
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
    public List<CartableDto> findByState(String state) {
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
