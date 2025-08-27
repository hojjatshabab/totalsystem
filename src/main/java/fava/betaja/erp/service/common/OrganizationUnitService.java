package fava.betaja.erp.service.common;

import fava.betaja.erp.dto.common.OrganizationUnitDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationUnitService {

    Page<OrganizationUnitDto> findAll(Pageable pageable);

    OrganizationUnitDto save(OrganizationUnitDto dto);

    public Optional<OrganizationUnitDto> findById(Long id);


    public Boolean deleteById(Long id);

}
