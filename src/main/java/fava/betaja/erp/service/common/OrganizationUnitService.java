package fava.betaja.erp.service.common;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.OrganizationUnitDto;

import java.util.List;
import java.util.Optional;

public interface OrganizationUnitService {
    OrganizationUnitDto save(OrganizationUnitDto organizationUnitDto);

    OrganizationUnitDto getCurrentOrganizationUnit();

    List<OrganizationUnitDto> findAllList();

    OrganizationUnitDto update(OrganizationUnitDto organizationUnitDto);

    PageResponse<OrganizationUnitDto> findAll(PageRequest<OrganizationUnitDto> model);

    Optional<OrganizationUnitDto> findById(Long id);

    Optional<OrganizationUnitDto> findByName(String name);

    Optional<List<OrganizationUnitDto>> findByCode(String code);

    Optional<List<OrganizationUnitDto>> findAllChildrenById(Long id);

    Optional<List<OrganizationUnitDto>> findByNameContains(String name);

    List<OrganizationUnitDto> findByCommonBase(String type,String data);

    Optional<List<OrganizationUnitDto>> findChildrenById(Long id);

    List<OrganizationUnitDto> findAllForceWithOutChildren();

    List<OrganizationUnitDto> findAllForceByParentWithOutChildren();

    OrganizationUnitDto getRootWithOutChildren();

    List<OrganizationUnitDto> findByParentByIdWithOutChildren(Long id);

    Optional<List<OrganizationUnitDto>> findAllParentByOrgId(Long id);

    Optional<OrganizationUnitDto> findParentByOrgId(Long id);

    Optional<List<OrganizationUnitDto>> findChildrenByCodePath(String code);

    Optional<List<OrganizationUnitDto>> findAllChildrenByCodePath(String name);

    OrganizationUnitDto findByParentIdIsNull();

    Boolean deleteById(Long id);

    String generateCodePath(OrganizationUnitDto organizationUnitDto);

    String generateCodePathByParentId(OrganizationUnitDto organizationUnitDto);


}
