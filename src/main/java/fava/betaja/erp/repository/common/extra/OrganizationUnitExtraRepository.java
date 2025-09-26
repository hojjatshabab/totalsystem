package fava.betaja.erp.repository.common.extra;

import fava.betaja.erp.entities.common.OrganizationUnit;

import java.util.List;
import java.util.Optional;

public interface OrganizationUnitExtraRepository {

    String findUniqueCodePathByParentCodePath(String parentCodePath);

    Optional<List<OrganizationUnit>> findByParentIdOrderByCodePathAsc(Long parentId);

    Optional<OrganizationUnit> findByUnitCode(String code);
}
