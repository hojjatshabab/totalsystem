package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockDto;
import fava.betaja.erp.dto.da.ProjectDto;
import fava.betaja.erp.entities.da.Block;
import fava.betaja.erp.entities.da.Project;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.BlockDtoMapper;
import fava.betaja.erp.mapper.da.ProjectDtoMapper;
import fava.betaja.erp.repository.da.BlockRepository;
import fava.betaja.erp.repository.da.PlanRepository;
import fava.betaja.erp.repository.da.ProjectRepository;
import fava.betaja.erp.service.da.BlockService;
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
public class BlockServiceImpl implements BlockService {

    private final BlockRepository repository;
    private final ProjectRepository projectRepository;
    private final BlockDtoMapper mapper;

    @Override
    public BlockDto save(BlockDto dto) {
        validate(dto, true);
        log.info("Saving Block: {}", dto.getName());
        Block entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public BlockDto update(BlockDto dto) {
        validate(dto, false);
        log.info("Updating Block: id={}, name={}", dto.getId(), dto.getName());
        Block entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<BlockDto> findAll(PageRequest<BlockDto> model) {
        List<BlockDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<BlockDto> findByProjectId(UUID planId, PageRequest<BlockDto> model) {
        List<BlockDto> result = repository
                .findByProjectId(planId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<BlockDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockDto> findAllListByProjectId(UUID projectId) {
        return repository.findByProjectId(projectId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BlockDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        Block entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("Block با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted Block: id={}, name={}", entity.getId(), entity.getName());
    }

    private void validate(BlockDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("Block برای بروزرسانی موجود نیست.");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام Block الزامی است.");
        }

        if (dto.getProjectId() == null) {
            throw new ServiceException("پروژه (Project) الزامی است.");
        }

        if (!projectRepository.existsById(dto.getProjectId())) {
            throw new ServiceException("پروژه انتخاب شده موجود نیست.");
        }

    }
}
