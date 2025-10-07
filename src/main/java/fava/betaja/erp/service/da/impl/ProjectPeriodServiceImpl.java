package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockDto;
import fava.betaja.erp.dto.da.ProjectPeriodDto;
import fava.betaja.erp.entities.da.Block;
import fava.betaja.erp.entities.da.ProjectPeriod;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.ProjectPeriodDtoMapper;
import fava.betaja.erp.repository.da.ProjectPeriodRepository;
import fava.betaja.erp.repository.da.PeriodRangeRepository;
import fava.betaja.erp.repository.da.ProjectRepository;
import fava.betaja.erp.service.da.ProjectPeriodService;
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
public class ProjectPeriodServiceImpl implements ProjectPeriodService {

    private final ProjectPeriodRepository repository;
    private final ProjectRepository projectRepository;
    private final PeriodRangeRepository periodRangeRepository;
    private final ProjectPeriodDtoMapper mapper;

    @Override
    public ProjectPeriodDto save(ProjectPeriodDto dto) {
        validate(dto, true);
        StringBuilder title = new StringBuilder();
        title.append(periodRangeRepository.findById(dto.getPeriodRangeId()).get().getName())
                        .append("، ")
                                .append(dto.getYear())
                                        .append(" - پروژه ")
                                                .append(projectRepository.findById(dto.getProjectId()).get().getName());
        dto.setTitle(title.toString());
        log.info("Saving ProjectPeriod: {}", dto.getTitle());
        ProjectPeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ProjectPeriodDto update(ProjectPeriodDto dto) {
        validate(dto, false);
        log.info("Updating ProjectPeriod: id={}, title={}", dto.getId(), dto.getTitle());
        ProjectPeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public Optional<ProjectPeriodDto> findByProjectIdAndIsActiveTrue(UUID projectId) {
        return Optional.ofNullable(projectId)
                .flatMap(repository::findByProjectIdAndIsActiveTrue)
                .map(mapper::toDto);
    }

    @Override
    public PageResponse<ProjectPeriodDto> findAll(PageRequest<ProjectPeriodDto> model) {
        List<ProjectPeriodDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<ProjectPeriodDto> findByProjectId(UUID projectId, PageRequest<ProjectPeriodDto> model) {
        Page<ProjectPeriod> page = repository.findByProjectId(
                projectId,
                Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1)
        );

        List<ProjectPeriodDto> result = page.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = page.getTotalElements();

        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<ProjectPeriodDto> findByProjectIdAndPeriodRangeIdAndYear(UUID projectId, UUID periodId, String year, PageRequest<ProjectPeriodDto> model) {
        Page<ProjectPeriod> page = repository.findByProjectIdAndPeriodRangeIdAndYear(
                projectId, periodId, year,
                Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1)
        );

        List<ProjectPeriodDto> result = page.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = page.getTotalElements();

        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<ProjectPeriodDto> findByPeriodRangeIdAndYear(UUID periodId, String year, PageRequest<ProjectPeriodDto> model) {
        Page<ProjectPeriod> page = repository.findByPeriodRangeIdAndYear(
                periodId, year,
                Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1)
        );

        List<ProjectPeriodDto> result = page.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = page.getTotalElements();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<ProjectPeriodDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectPeriodDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        ProjectPeriod entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("ProjectPeriod با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted ProjectPeriod: id={}, title={}", entity.getId(), entity.getTitle());
    }

    private void validate(ProjectPeriodDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("ProjectPeriod برای بروزرسانی موجود نیست.");
        }

        if (dto.getYear() == null || dto.getYear().isBlank()) {
            throw new ServiceException("سال الزامی است.");
        }

        if (!projectRepository.existsById(dto.getProjectId())) {
            throw new ServiceException("پروژه انتخاب شده موجود نیست.");
        }
        if (!periodRangeRepository.existsById(dto.getPeriodRangeId())) {
            throw new ServiceException("دوره بلوک انتخاب شده موجود نیست.");
        }

    }
}
