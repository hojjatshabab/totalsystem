package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.DataPeriodDto;
import fava.betaja.erp.entities.da.Attribute;
import fava.betaja.erp.entities.da.DataPeriod;
import fava.betaja.erp.mapper.da.DataPeriodDtoMapper;
import fava.betaja.erp.repository.da.DataPeriodRepository;
import fava.betaja.erp.service.da.DataPeriodService;
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
public class DataPeriodServiceImpl implements DataPeriodService {

    private final DataPeriodRepository dataPeriodRepository;
    private final DataPeriodDtoMapper dataPeriodDtoMapper;

    @Override
    public DataPeriodDto save(DataPeriodDto dataPeriodDto) {
        log.info("Save DataPeriodDto .");
        return dataPeriodDtoMapper.toDto(dataPeriodRepository.save(
                dataPeriodDtoMapper.toEntity(dataPeriodDto)
        ));
    }

    @Override
    public DataPeriodDto update(DataPeriodDto dataPeriodDto) {
        log.info("Update DataPeriodDto .");
        return dataPeriodDtoMapper.toDto(dataPeriodRepository.save(
                dataPeriodDtoMapper.toEntity(dataPeriodDto)
        ));
    }

    @Override
    public PageResponse<DataPeriodDto> findAll(PageRequest<DataPeriodDto> model) {
        List<DataPeriodDto> result = dataPeriodRepository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(dataPeriodDtoMapper::toDto)
                .collect(Collectors.toList());
        long count = dataPeriodRepository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<DataPeriodDto> findAll() {
        List<DataPeriod> result = dataPeriodRepository.findAll();
        if (result.isEmpty()) {
            return List.of();
        }
        return result.stream().map(dataPeriodDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<DataPeriodDto> findById(UUID id) {
        Optional<DataPeriod> optionalDataPeriod = dataPeriodRepository.findById(id);
        if (optionalDataPeriod.isPresent()) {
            return Optional.ofNullable(dataPeriodDtoMapper.toDto(optionalDataPeriod.get()));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (id == null || !findById(id).isPresent()) {
            return false;
        }
        dataPeriodRepository.deleteById(id);
        return true;
    }


}
