package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.PlanDto;
import fava.betaja.erp.dto.da.ProjectDto;
import fava.betaja.erp.entities.da.Attribute;
import fava.betaja.erp.entities.da.Plan;
import fava.betaja.erp.entities.da.Project;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.AttributeDtoMapper;
import fava.betaja.erp.mapper.da.PlanDtoMapper;
import fava.betaja.erp.repository.common.OrganizationUnitRepository;
import fava.betaja.erp.repository.da.AttributeRepository;
import fava.betaja.erp.repository.da.PlanRepository;
import fava.betaja.erp.service.da.AttributeService;
import fava.betaja.erp.service.da.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
public class PlanServiceImpl implements PlanService {

    private final PlanRepository repository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final PlanDtoMapper mapper;

    @Override
    public PlanDto save(PlanDto dto) {
        validate(dto, true);
        log.info("Saving Plan: {}", dto.getName());
        Plan entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PlanDto update(PlanDto dto) {
        validate(dto, false);
        log.info("Updating Plan: id={}, name={}", dto.getId(), dto.getName());
        Plan entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<PlanDto> findAll(PageRequest<PlanDto> model) {
        List<PlanDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<PlanDto> findByOrganizationUnitId(Long organizationUnitId, PageRequest<PlanDto> model) {
        Page<Plan> page = repository.findByOrganizationUnitId(
                organizationUnitId,
                Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1)
        );

        List<PlanDto> result = page.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = page.getTotalElements();

        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<PlanDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PlanDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        Plan plan = repository.findById(id)
                .orElseThrow(() -> new ServiceException("Plan با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted Plan: id={}, name={}", plan.getId(), plan.getName());
    }

    private void validate(PlanDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("Plan برای بروزرسانی موجود نیست.");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام Plan الزامی است.");
        }

        if (dto.getOrganizationUnitId() == null) {
            throw new ServiceException("یگان (OrganizationUnit) الزامی است.");
        }

        if (!organizationUnitRepository.existsById(dto.getOrganizationUnitId())) {
            throw new ServiceException("یگان انتخاب شده موجود نیست.");
        }

    }
}
