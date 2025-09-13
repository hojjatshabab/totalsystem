package fava.betaja.erp.service.common.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.mapper.common.OrganizationUnitDtoMapper;
import fava.betaja.erp.repository.common.OrganizationUnitRepository;
import fava.betaja.erp.service.common.OrganizationUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationUnitServiceImpl implements OrganizationUnitService {

    private final OrganizationUnitRepository organizationUnitRepository;
    private final OrganizationUnitDtoMapper organizationUnitDtoMapper;

    @Override
    public OrganizationUnitDto save(OrganizationUnitDto organizationUnitDto) {
        log.info("Save OrganizationUnit name {} .", organizationUnitDto.getName());
        return organizationUnitDtoMapper.toDto(organizationUnitRepository.save(
                organizationUnitDtoMapper.toEntity(organizationUnitDto)
        ));
    }

    @Override
    public OrganizationUnitDto update(OrganizationUnitDto organizationUnitDto) {
        log.info("Update OrganizationUnit name {} .", organizationUnitDto.getName());
        return organizationUnitDtoMapper.toDto(organizationUnitRepository.save(
                organizationUnitDtoMapper.toEntity(organizationUnitDto)
        ));
    }

    @Override
    public PageResponse<OrganizationUnitDto> findAll(PageRequest<OrganizationUnitDto> model) {
        List<OrganizationUnitDto> result = organizationUnitRepository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(organizationUnitDtoMapper::toDto)
                .collect(Collectors.toList());
        long count = organizationUnitRepository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<OrganizationUnitDto> findAll() {
        List<OrganizationUnit> result = organizationUnitRepository.findAll();
        if (result.isEmpty()) {
            return List.of();
        }
        return result.stream().map(organizationUnitDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<OrganizationUnitDto> findById(Long id) {
        Optional<OrganizationUnit> optionalOrganizationUnit = organizationUnitRepository.findById(id);
        if (optionalOrganizationUnit.isPresent()) {
            return Optional.ofNullable(organizationUnitDtoMapper.toDto(optionalOrganizationUnit.get()));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(Long id) {
        if (id == null || !findById(id).isPresent()) {
            return false;
        }
        organizationUnitRepository.deleteById(id);
        return true;
    }
}
