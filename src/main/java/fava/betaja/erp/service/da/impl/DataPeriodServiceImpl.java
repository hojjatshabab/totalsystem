package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.entities.da.Attribute;
import fava.betaja.erp.mapper.da.AttributeDtoMapper;
import fava.betaja.erp.repository.da.AttributeRepository;
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

    private final AttributeRepository attributeRepository;
    private final AttributeDtoMapper attributeDtoMapper;

    @Override
    public AttributeDto save(AttributeDto attributeDto) {
        log.info("Save attribute name {} .", attributeDto.getName());
        return attributeDtoMapper.toDto(attributeRepository.save(
                attributeDtoMapper.toEntity(attributeDto)
        ));
    }

    @Override
    public AttributeDto update(AttributeDto attributeDto) {
        log.info("Update attribute name {} .", attributeDto.getName());
        return attributeDtoMapper.toDto(attributeRepository.save(
                attributeDtoMapper.toEntity(attributeDto)
        ));
    }

    @Override
    public PageResponse<AttributeDto> findAll(PageRequest<AttributeDto> model) {
        List<AttributeDto> result = attributeRepository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(attributeDtoMapper::toDto)
                .collect(Collectors.toList());
        long count = attributeRepository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<AttributeDto> findAll() {
        List<Attribute> result = attributeRepository.findAll();
        if (result.isEmpty()) {
            return List.of();
        }
        return result.stream().map(attributeDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<AttributeDto> findById(UUID id) {
        Optional<Attribute> optionalAttribute = attributeRepository.findById(id);
        if (optionalAttribute.isPresent()) {
            return Optional.ofNullable(attributeDtoMapper.toDto(optionalAttribute.get()));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (id == null || !findById(id).isPresent()) {
            return false;
        }
        attributeRepository.deleteById(id);
        return true;
    }


}
