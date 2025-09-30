package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockValueDto;
import fava.betaja.erp.entities.da.Block;
import fava.betaja.erp.entities.da.BlockValue;
import fava.betaja.erp.entities.da.ProjectPeriod;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.BlockValueDtoMapper;
import fava.betaja.erp.repository.da.BlockRepository;
import fava.betaja.erp.repository.da.BlockValueRepository;
import fava.betaja.erp.repository.da.ProjectPeriodRepository;
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
    private final ProjectPeriodRepository projectPeriodRepository;
    private final BlockRepository blockRepository;
    private final BlockValueDtoMapper mapper;

    @Override
    public BlockValueDto save(BlockValueDto dto) {
        validate(dto, true);
        Block block = blockRepository.findById(dto.getBlockId()).get();
        Optional<ProjectPeriod> optionalProjectPeriod = projectPeriodRepository
                .findByProjectIdAndIsActiveTrue(block.getProject().getId());
        if (!optionalProjectPeriod.isPresent()){
            throw new ServiceException("دوره زمانی فعالی موجود نیست.");
        }
        dto.setProjectPeriodId(optionalProjectPeriod.get().getId());
        dto.setName(block.getName());
        log.info("Saving BlockValue: {}", dto.getName());
        BlockValue entity = mapper.toEntity(dto);
        BlockValueDto result = mapper.toDto(repository.save(entity));

        block.setBlockCount(result.getBlockCount());
        block.setDeliveryDate(result.getDeliveryDate());
        block.setStartDate(result.getStartDate());
        block.setFloorCount(result.getFloorCount());
        block.setUnitCount(result.getUnitCount());
        block.setTotalArea(result.getTotalArea());

        blockRepository.save(block);

        return result;
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
    public PageResponse<BlockValueDto> findByProjectPeriodId(UUID projectPeriodId, PageRequest<BlockValueDto> model) {
        List<BlockValueDto> result = repository
                .findByProjectPeriodId(projectPeriodId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<BlockValueDto> findByProjectPeriodIdAndBlockId(UUID projectPeriodId, UUID blockId, PageRequest<BlockValueDto> model) {
        List<BlockValueDto> result = repository
                .findByProjectPeriodIdAndBlockId(projectPeriodId, blockId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<BlockValueDto> findByBlockId(UUID blockId, PageRequest<BlockValueDto> model) {
        List<BlockValueDto> result = repository
                .findByBlockId(blockId,
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
        if (!blockRepository.existsById(dto.getBlockId())) {
            throw new ServiceException("بلوک انتخاب شده موجود نیست.");
        }

    }
}
