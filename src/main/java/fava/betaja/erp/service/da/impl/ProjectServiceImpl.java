package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.PlanDto;
import fava.betaja.erp.dto.da.ProjectDto;
import fava.betaja.erp.entities.da.Plan;
import fava.betaja.erp.entities.da.Project;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.ProjectDtoMapper;
import fava.betaja.erp.repository.da.PlanRepository;
import fava.betaja.erp.repository.da.ProjectRepository;
import fava.betaja.erp.service.da.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;
    private final PlanRepository planRepository;
    private final ProjectDtoMapper mapper;

    @Override
    public ProjectDto save(ProjectDto dto) {
        validate(dto, true);
        log.info("Saving Project: {}", dto.getName());
        Project entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ProjectDto update(ProjectDto dto) {
        validate(dto, false);
        log.info("Updating Project: id={}, name={}", dto.getId(), dto.getName());
        Project entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<ProjectDto> findAll(PageRequest<ProjectDto> model) {
        List<ProjectDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<ProjectDto> findByPlanId(UUID planId, PageRequest<ProjectDto> model) {
        List<ProjectDto> result = repository
                .findByPlanId(planId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<ProjectDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        Project entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("Project با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted Project: id={}, name={}", entity.getId(), entity.getName());
    }

    private void validate(ProjectDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("project برای بروزرسانی موجود نیست.");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام project الزامی است.");
        }

        if (dto.getPlanId() == null) {
            throw new ServiceException("طرح (Plan) الزامی است.");
        }

        if (!planRepository.existsById(dto.getPlanId())) {
            throw new ServiceException("طرح انتخاب شده موجود نیست.");
        }

    }
}
