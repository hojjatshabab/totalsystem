package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleStepDto;
import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.baseinfo.FlowRuleStepDtoMapper;
import fava.betaja.erp.repository.baseinfo.FlowRuleStepRepository;
import fava.betaja.erp.service.baseinfo.FlowRuleStepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FlowRuleStepServiceImpl implements FlowRuleStepService {

    private final FlowRuleStepRepository repository;
    private final FlowRuleStepDtoMapper mapper;

    @Override
    public FlowRuleStepDto save(FlowRuleStepDto dto) {
        validate(dto, true);
        log.info("Saving FlowRuleStep: stepOrder={}, role={}", dto.getStepOrder(), dto.getRoleName());
        FlowRuleStep entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public FlowRuleStepDto update(FlowRuleStepDto dto) {
        validate(dto, false);
        log.info("Updating FlowRuleStep: id={}, stepOrder={}", dto.getId(), dto.getStepOrder());
        FlowRuleStep entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<FlowRuleStepDto> findAll(PageRequest<FlowRuleStepDto> model) {
        List<FlowRuleStepDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<FlowRuleStepDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowRuleStepDto> findByFlowRuleIdOrderByStepOrder(UUID flowRuleId) {
        return repository.findByFlowRuleIdOrderByStepOrderAsc(flowRuleId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowRuleStepDto> findByPreviousStepId(UUID previousStepId) {
        return repository.findByPreviousStepId(previousStepId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FlowRuleStepDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        FlowRuleStep step = repository.findById(id)
                .orElseThrow(() -> new ServiceException("FlowRuleStep با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted FlowRuleStep: id={}, stepOrder={}", step.getId(), step.getStepOrder());
    }

    private void validate(FlowRuleStepDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("FlowRuleStep برای بروزرسانی موجود نیست.");
        }

        if (dto.getStepOrder() == null) {
            throw new ServiceException("ترتیب مرحله (stepOrder) الزامی است.");
        }

        if (dto.getRoleId() == null) {
            throw new ServiceException("نقش (role) الزامی است.");
        }

        if (dto.getFlowRuleId() == null) {
            throw new ServiceException("شناسه FlowRule الزامی است.");
        }
    }
}

