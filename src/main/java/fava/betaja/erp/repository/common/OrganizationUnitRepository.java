package fava.betaja.erp.repository.common;

import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.repository.common.extra.OrganizationUnitExtraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationUnitRepository extends JpaRepository<OrganizationUnit, Long>, OrganizationUnitExtraRepository {
    OrganizationUnit findByParentIdIsNull();

    List<OrganizationUnit> findByParent(OrganizationUnit parent);

    Optional<OrganizationUnit> findByName(String name);

    List<OrganizationUnit> findByCommonBaseDataOrgTypeId(Long id);

    Optional<OrganizationUnit> findByCodePath(String codePath);

    Optional<List<OrganizationUnit>> findByParentIdOrderByCodePathDesc(Long parentId);

    Optional<List<OrganizationUnit>> findByCode(String code);

    Optional<List<OrganizationUnit>> findByNameContains(String name);

    @Query(value = "SELECT o FROM OrganizationUnit o WHERE o.codePath like :codePath order by o.codePath asc ")
    Optional<List<OrganizationUnit>> findAllChildrenByCodePath(@Param("codePath") String codePath);

    @Override
    Page<OrganizationUnit> findAll(Pageable pageable);
}
