package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockValueDto;
import fava.betaja.erp.entities.da.BlockValue;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.BlockValueDtoMapper;
import fava.betaja.erp.repository.da.BlockPeriodRepository;
import fava.betaja.erp.repository.da.BlockValueRepository;
import fava.betaja.erp.service.da.BlockValueService;
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
public class BlockValueServiceImpl implements BlockValueService {

    private final BlockValueRepository repository;
    private final BlockPeriodRepository blockPeriodRepository;
    private final BlockValueDtoMapper mapper;

    @Override
    public BlockValueDto save(BlockValueDto dto) {
        validate(dto, true);
        log.info("Saving BlockValue: {}", dto.getName());
        BlockValue entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
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
    public PageResponse<BlockValueDto> findByBlockPeriodId(UUID blockPeriodId, PageRequest<BlockValueDto> model) {
        List<BlockValueDto> result = repository
                .findByBlockPeriodId(blockPeriodId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
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

        if (!blockPeriodRepository.existsById(dto.getBlockPeriodId())) {
            throw new ServiceException("پروژه انتخاب شده موجود نیست.");
        }

    }
}
