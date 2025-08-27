package fava.betaja.erp.service.common.impl;

import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.mapper.common.OrganizationUnitMapper;
import fava.betaja.erp.repository.common.OrganizationUnitRepository;
import fava.betaja.erp.service.common.OrganizationUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationUnitServiceImpl implements OrganizationUnitService {

    @Autowired
    OrganizationUnitRepository repository;

    @Autowired
    OrganizationUnitMapper mapper;

    @Override
    public Page<OrganizationUnitDto> findAll(Pageable model) {

        Page<OrganizationUnit> entityPage=repository.findAll(model);
        List<OrganizationUnit> entityList= entityPage.stream().toList();
        List<OrganizationUnitDto> dtoList=mapper.toDtoList(entityList);

        return new PageImpl<>(dtoList, model, entityPage.getTotalElements());

    }

    @Override
    public OrganizationUnitDto save(OrganizationUnitDto organizationUnitDto) {
        OrganizationUnit entity =  mapper.toEntity(organizationUnitDto);
        OrganizationUnit savedEntity =repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public Optional<OrganizationUnitDto> findById(Long id) {
        Optional<OrganizationUnit> optional=repository.findById(id);
        if (optional.isPresent())
            return Optional.of(mapper.toDto(optional.get()));
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(Long id) {
        repository.deleteById(id);
        return true;
    }
}
