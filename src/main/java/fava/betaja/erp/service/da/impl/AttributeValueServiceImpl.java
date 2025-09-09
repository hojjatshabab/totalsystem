package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeValueDto;
import fava.betaja.erp.entities.da.AttributeValue;
import fava.betaja.erp.mapper.da.AttributeValueDtoMapper;
import fava.betaja.erp.repository.da.AttributeValueRepository;
import fava.betaja.erp.service.da.AttributeValueService;
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
public class AttributeValueServiceImpl implements AttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeValueDtoMapper attributeValueDtoMapper;

    @Override
    public AttributeValueDto save(AttributeValueDto attributeValueDto) {
        log.info("Save AttributeValue name .");
        return attributeValueDtoMapper.toDto(attributeValueRepository.save(
                attributeValueDtoMapper.toEntity(attributeValueDto)
        ));
    }

    @Override
    public AttributeValueDto update(AttributeValueDto attributeValueDto) {
        log.info("Update attribute name .");
        return attributeValueDtoMapper.toDto(attributeValueRepository.save(
                attributeValueDtoMapper.toEntity(attributeValueDto)
        ));
    }

    @Override
    public PageResponse<AttributeValueDto> findAll(PageRequest<AttributeValueDto> model) {
        List<AttributeValueDto> result = attributeValueRepository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(attributeValueDtoMapper::toDto)
                .collect(Collectors.toList());
        long count = attributeValueRepository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<AttributeValueDto> findAll() {
        List<AttributeValue> result = attributeValueRepository.findAll();
        if (result.isEmpty()) {
            return List.of();
        }
        return result.stream().map(attributeValueDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<AttributeValueDto> findById(UUID id) {
        Optional<AttributeValue> optionalAttributeValue = attributeValueRepository.findById(id);
        if (optionalAttributeValue.isPresent()) {
            return Optional.ofNullable(attributeValueDtoMapper.toDto(optionalAttributeValue.get()));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (id == null || !findById(id).isPresent()) {
            return false;
        }
        attributeValueRepository.deleteById(id);
        return true;
    }


}
