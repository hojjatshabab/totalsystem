package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.entities.da.Attribute;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.AttributeDtoMapper;
import fava.betaja.erp.repository.common.OrganizationUnitRepository;
import fava.betaja.erp.repository.da.AttributeRepository;
import fava.betaja.erp.service.da.AttributeService;
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
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository repository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final AttributeDtoMapper mapper;

    @Override
    public AttributeDto save(AttributeDto dto) {
        validate(dto, true);
        log.info("Saving Attribute: {}", dto.getName());
        Attribute entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public AttributeDto update(AttributeDto dto) {
        validate(dto, false);
        log.info("Updating Attribute: id={}, name={}", dto.getId(), dto.getName());
        Attribute entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<AttributeDto> findAll(PageRequest<AttributeDto> model) {
        List<AttributeDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<AttributeDto> findByOrganizationUnitId(Long organizationUnitId, PageRequest<AttributeDto> model) {
        List<AttributeDto> result = repository
                .findByOrganizationUnitId(organizationUnitId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<AttributeDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttributeDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        Attribute attribute = repository.findById(id)
                .orElseThrow(() -> new ServiceException("Attribute با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted Attribute: id={}, name={}", attribute.getId(), attribute.getName());
    }

    private void validate(AttributeDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("Attribute برای بروزرسانی موجود نیست.");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام Attribute الزامی است.");
        }

        if (dto.getOrganizationUnitId() == null) {
            throw new ServiceException("یگان (OrganizationUnit) الزامی است.");
        }

        if (!organizationUnitRepository.existsById(dto.getOrganizationUnitId())) {
            throw new ServiceException("یگان انتخاب شده موجود نیست.");
        }

    }
}
