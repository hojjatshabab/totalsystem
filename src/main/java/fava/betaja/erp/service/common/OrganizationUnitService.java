package fava.betaja.erp.service.common;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.OrganizationUnitDto;

import java.util.List;
import java.util.Optional;

public interface OrganizationUnitService {
    OrganizationUnitDto save(OrganizationUnitDto organizationUnitDto);

    OrganizationUnitDto update(OrganizationUnitDto organizationUnitDto);

    PageResponse<OrganizationUnitDto> findAll(PageRequest<OrganizationUnitDto> model);

    List<OrganizationUnitDto> findAll();

    Optional<OrganizationUnitDto> findById(Long id);

    Boolean deleteById(Long id);

}
