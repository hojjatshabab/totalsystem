package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockValueDto;
import fava.betaja.erp.dto.security.UsersDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.entities.baseinfo.CartableHistory;
import fava.betaja.erp.entities.baseinfo.FlowRuleDomain;
import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import fava.betaja.erp.entities.da.*;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.enums.baseinfo.ActionTypeEnum;
import fava.betaja.erp.enums.baseinfo.CartableState;
import fava.betaja.erp.enums.baseinfo.Priority;
import fava.betaja.erp.enums.da.BlockValueState;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.BlockValueDtoMapper;
import fava.betaja.erp.mapper.security.UsersDtoMapper;
import fava.betaja.erp.repository.baseinfo.CartableHistoryRepository;
import fava.betaja.erp.repository.baseinfo.CartableRepository;
import fava.betaja.erp.repository.baseinfo.FlowRuleDomainRepository;
import fava.betaja.erp.repository.baseinfo.FlowRuleStepRepository;
import fava.betaja.erp.repository.da.*;
import fava.betaja.erp.repository.security.UserRepository;
import fava.betaja.erp.repository.security.UserRoleRepository;
import fava.betaja.erp.service.da.BlockValueService;
import fava.betaja.erp.service.security.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BlockValueServiceImpl implements BlockValueService {

    private final BlockValueRepository repository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PlanRepository planRepository;
    private final UsersService usersService;
    private final UsersDtoMapper usersDtoMapper;
    private final ProjectPeriodRepository projectPeriodRepository;
    private final BlockRepository blockRepository;
    private final CartableRepository cartableRepository;
    private final UserRoleRepository userRoleRepository;
    private final CartableHistoryRepository cartableHistoryRepository;
    private final FlowRuleStepRepository flowRuleStepRepository;
    private final FlowRuleDomainRepository flowRuleDomainRepository;
    private final BlockValueDtoMapper mapper;

    @Override
    @Transactional
    public BlockValueDto save(BlockValueDto dto) {
        validate(dto, true);

        Block block = blockRepository.findById(dto.getBlockId())
                .orElseThrow(() -> new ServiceException("بلوک مورد نظر یافت نشد."));

        if (dto.getProjectPeriodId() == null) {
            throw new ServiceException("اطلاعات دوره زمانی نمیتواند خالی باشد.");
        }
        ProjectPeriod projectPeriod = projectPeriodRepository
                .findById(dto.getProjectPeriodId())
                .orElseThrow(() -> new ServiceException("دوره زمانی فعالی برای این پروژه وجود ندارد."));
        dto.setProjectPeriodId(projectPeriod.getId());
        dto.setName(projectPeriod.getTitle());
        dto.setBlockValueState(BlockValueState.IN_PROGRESS);

        BlockValue entity = mapper.toEntity(dto);
        BlockValue saved = repository.save(entity);

        block.setBlockCount(saved.getBlockCount());
        block.setFloorCount(saved.getFloorCount());
        block.setUnitCount(saved.getUnitCount());
        block.setTotalArea(saved.getTotalArea());
        block.setDeliveryDate(saved.getDeliveryDate());
        block.setStartDate(saved.getStartDate());
        blockRepository.save(block);

        FlowRuleDomain flowRuleDomain = flowRuleDomainRepository
                .findFirstCandidate("block-value", "company");
        if (flowRuleDomain == null) {
            throw new ServiceException("جریان کاری برای BlockValue تعریف نشده است.");
        }

        FlowRuleStep firstStep = flowRuleStepRepository
                .findFirstByFlowRuleIdOrderByStepOrder(flowRuleDomain.getFlowRule().getId())
                .orElseThrow(() -> new ServiceException("مرحله اول جریان یافت نشد."));

        Users currentUser = usersDtoMapper.toEntity(usersService.getCurrentUser());

        boolean validRole = userRoleRepository.findByUserId(currentUser.getId()).stream()
                .anyMatch(userRole -> userRole.getRole().getName().contains("ROLE_COMPANY"));

        if (!validRole) {
            throw new ServiceException("شما مجاز به انجام اقدام در این مرحله نیستید.");
        }

        Users recipient = userRepository.findFirstByRoleIdAndOrganizationUnitId(
                firstStep.getRole().getId(),
                block.getProject().getPlan().getOrganizationUnit().getId()
        );

        if (recipient == null) {
            throw new ServiceException("کاربری با نقش " + firstStep.getRole().getName() +
                    " در یگان " + block.getProject().getPlan().getOrganizationUnit().getName() + " یافت نشد.");
        }

        Cartable cartable = new Cartable();
        cartable.setTitle(saved.getName());
        cartable.setDocumentId(saved.getId());
        cartable.setDocumentNumber("BV-" + saved.getId().toString().substring(0, 8));
        cartable.setState(CartableState.PENDING);
        cartable.setSender(currentUser);
        cartable.setRecipient(recipient);
        cartable.setFlowRuleDomain(flowRuleDomain);
        cartable.setCurrentStep(firstStep);
        cartable.setPriority(Priority.NORMAL);
        cartable.setRead(false);
        cartable.setSendDate(LocalDate.now());

        cartableRepository.save(cartable);

        CartableHistory history = new CartableHistory();
        history.setCartable(cartable);
        history.setUser(cartable.getSender());
        history.setActionType(ActionTypeEnum.SUBMIT);
        history.setActionDate(new Date());
        history.setComment("ایجاد مقدار جدید بلوک و ارسال به مرحله اول جریان کاری");
        cartableHistoryRepository.save(history);

        log.info("BlockValue '{}' created and sent to cartable for recipient '{}'", saved.getName(), recipient.getUsername());

        return mapper.toDto(saved);
    }

    @Override
    public BlockValueDto update(BlockValueDto dto) {
        validate(dto, false);
        log.info("Updating BlockValue: id={}, name={}", dto.getId(), dto.getName());
        BlockValue entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<BlockValueDto> findAll(PageRequest<BlockValueDto> model) {
        List<BlockValueDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<BlockValueDto> findByCompany() {
        UsersDto usersDto = usersService.getCurrentUser();
        if (usersDto.getOrganizationUnitId() == null) {
            throw new ServiceException(" یگان کاربر یافت نشد. ");
        }
        List<Plan> plans = planRepository.findByOrganizationUnitId(usersDto.getOrganizationUnitId());
        if (plans.size() == 0) {
            throw new ServiceException(" هیچ طرحی وجود ندارد. ");
        }
        List<ProjectPeriod> projectPeriods = new ArrayList<>();
        for (Plan plan : plans) {
            List<Project> projects = projectRepository.findByPlanId(plan.getId());
            if (projects.size() != 0) {
                for (Project project : projects) {
                    projectPeriods.addAll(projectPeriodRepository.findByProjectId(project.getId()));
                }
            }
        }
        List<BlockValue> result = new ArrayList<>();
        for (ProjectPeriod projectPeriod : projectPeriods) {
            result.addAll(repository.findByProjectPeriodId(projectPeriod.getId()));
        }

        return mapper.toDtoList(result);
    }

    @Override
    public PageResponse<BlockValueDto> findByProjectPeriodId(UUID projectPeriodId, PageRequest<BlockValueDto> model) {
        Page<BlockValue> page = repository.findByProjectPeriodId(
                projectPeriodId,
                Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1)
        );

        List<BlockValueDto> result = page.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = page.getTotalElements();

        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<BlockValueDto> findByProjectPeriodIdAndBlockId(UUID projectPeriodId, UUID blockId, PageRequest<BlockValueDto> model) {
        Page<BlockValue> page = repository.findByProjectPeriodIdAndBlockId(
                projectPeriodId, blockId,
                Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1)
        );

        List<BlockValueDto> result = page.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = page.getTotalElements();

        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<BlockValueDto> findByBlockId(UUID blockId, PageRequest<BlockValueDto> model) {

        Page<BlockValue> page = repository.findByBlockId(
                blockId,
                Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1)
        );

        List<BlockValueDto> result = page.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = page.getTotalElements();

        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<BlockValueDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BlockValueDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        BlockValue entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("BlockValue با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted BlockValue: id={}, name={}", entity.getId(), entity.getName());
    }

    private void validate(BlockValueDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("BlockValue برای بروزرسانی موجود نیست.");
        }
        if (!blockRepository.existsById(dto.getBlockId())) {
            throw new ServiceException("بلوک انتخاب شده موجود نیست.");
        }

    }
}
