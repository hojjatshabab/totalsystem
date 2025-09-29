package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockPeriodDto;
import fava.betaja.erp.entities.da.BlockPeriod;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.BlockPeriodDtoMapper;
import fava.betaja.erp.repository.da.BlockPeriodRepository;
import fava.betaja.erp.repository.da.BlockRepository;
import fava.betaja.erp.repository.da.PeriodRangeRepository;
import fava.betaja.erp.service.da.BlockPeriodService;
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
public class BlockPeriodServiceImpl implements BlockPeriodService {

    private final BlockPeriodRepository repository;
    private final BlockRepository blockRepository;
    private final PeriodRangeRepository periodRangeRepository;
    private final BlockPeriodDtoMapper mapper;

    @Override
    public BlockPeriodDto save(BlockPeriodDto dto) {
        validate(dto, true);
        log.info("Saving BlockPeriod: {}", dto.getTitle());
        BlockPeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public BlockPeriodDto update(BlockPeriodDto dto) {
        validate(dto, false);
        log.info("Updating BlockPeriod: id={}, title={}", dto.getId(), dto.getTitle());
        BlockPeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<BlockPeriodDto> findAll(PageRequest<BlockPeriodDto> model) {
        List<BlockPeriodDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<BlockPeriodDto> findByBlockId(UUID blockId, PageRequest<BlockPeriodDto> model) {
        List<BlockPeriodDto> result = repository
                .findByBlockId(blockId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<BlockPeriodDto> findByBlockIdAndPeriodRangeIdAndYear(UUID blockId, UUID periodId, String year, PageRequest<BlockPeriodDto> model) {
        List<BlockPeriodDto> result = repository
                .findByBlockIdAndPeriodRangeIdAndYear(blockId, periodId, year,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<BlockPeriodDto> findByPeriodRangeIdAndYear(UUID periodId, String year, PageRequest<BlockPeriodDto> model) {
        List<BlockPeriodDto> result = repository
                .findByPeriodRangeIdAndYear(periodId, year,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<BlockPeriodDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BlockPeriodDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        BlockPeriod entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("BlockPeriod با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted Block: id={}, title={}", entity.getId(), entity.getTitle());
    }

    private void validate(BlockPeriodDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("BlockPeriod برای بروزرسانی موجود نیست.");
        }

        if (dto.getYear() == null || dto.getYear().isBlank()) {
            throw new ServiceException("سال الزامی است.");
        }

        if (!blockRepository.existsById(dto.getBlockId())) {
            throw new ServiceException("بلوک انتخاب شده موجود نیست.");
        }
        if (!periodRangeRepository.existsById(dto.getPeriodRangeId())) {
            throw new ServiceException("دوره بلوک انتخاب شده موجود نیست.");
        }

    }
}
