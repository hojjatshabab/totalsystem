package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.PeriodTypeDto;
import fava.betaja.erp.entities.da.PeriodType;
import fava.betaja.erp.mapper.da.PeriodTypeDtoMapper;
import fava.betaja.erp.repository.da.PeriodTypeRepository;
import fava.betaja.erp.service.da.PeriodTypeService;
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
public class PeriodTypeServiceImpl implements PeriodTypeService {

    private final PeriodTypeRepository periodTypeRepository;
    private final PeriodTypeDtoMapper periodTypeDtoMapper;

    @Override
    public PeriodTypeDto save(PeriodTypeDto periodTypeDto) {
        log.info("Save PeriodType name {} .", periodTypeDto.getName());
        return periodTypeDtoMapper.toDto(periodTypeRepository.save(
                periodTypeDtoMapper.toEntity(periodTypeDto)
        ));
    }

    @Override
    public PeriodTypeDto update(PeriodTypeDto periodTypeDto) {
        log.info("Update PeriodType name {} .", periodTypeDto.getName());
        return periodTypeDtoMapper.toDto(periodTypeRepository.save(
                periodTypeDtoMapper.toEntity(periodTypeDto)
        ));
    }

    @Override
    public PageResponse<PeriodTypeDto> findAll(PageRequest<PeriodTypeDto> model) {
        List<PeriodTypeDto> result = periodTypeRepository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(periodTypeDtoMapper::toDto)
                .collect(Collectors.toList());
        long count = periodTypeRepository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<PeriodTypeDto> findAll() {
        List<PeriodType> result = periodTypeRepository.findAll();
        if (result.isEmpty()) {
            return List.of();
        }
        return result.stream().map(periodTypeDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PeriodTypeDto> findById(UUID id) {
        Optional<PeriodType> optionalPeriodType = periodTypeRepository.findById(id);
        if (optionalPeriodType.isPresent()) {
            return Optional.ofNullable(periodTypeDtoMapper.toDto(optionalPeriodType.get()));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (id == null || !findById(id).isPresent()) {
            return false;
        }
        periodTypeRepository.deleteById(id);
        return true;
    }
}
