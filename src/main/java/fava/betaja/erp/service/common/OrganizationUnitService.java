package fava.betaja.erp.service.common;

import fava.betaja.erp.dto.common.OrganizationUnitDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrganizationUnitService {

    Page<OrganizationUnitDto> findAll(Pageable pageable);

    OrganizationUnitDto save(OrganizationUnitDto dto);

    Optional<OrganizationUnitDto> findById(Long id);

    Boolean deleteById(Long id);

}
