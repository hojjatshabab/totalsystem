package fava.betaja.erp.service.common.impl;

import fava.betaja.erp.dto.common.CommonTypeDto;
import fava.betaja.erp.entities.common.CommonType;
import fava.betaja.erp.mapper.common.CommonTypeDtoMapper;
import fava.betaja.erp.repository.common.CommonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommonTypeService implements fava.betaja.erp.service.common.CommonTypeService {


   @Autowired
   CommonTypeRepository repository;

   @Autowired
    CommonTypeDtoMapper mapper;

    @Override
    public Page<CommonTypeDto> findAll(Pageable pageable) {

        Page<CommonType> page = repository.findAll(pageable);
        return page.map(mapper::toDto);
    }

    @Override
    public Page<CommonTypeDto> findByName(Pageable pageable, String name) {
        Page<CommonType> page = repository.findCommonTypeByTypeNameContainingIgnoreCase(name,pageable);
        return page.map(mapper::toDto);
    }

    @Override
    public CommonTypeDto save(CommonTypeDto dto) {
        return
       mapper.toDto(repository.save(mapper.toEntity(dto)));

    }

    @Override
    public Optional<CommonTypeDto> findById(Long id) {

        Optional<CommonType> optional=  repository.findById(id);
        if (optional.isPresent())
                return optional.of( mapper.toDto(optional.get()));
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(Long id) {
        repository.deleteById(id);
        return true;
    }
}
