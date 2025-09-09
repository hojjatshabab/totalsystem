package fava.betaja.erp.repository.common;

import fava.betaja.erp.entities.common.OrganizationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationUnitRepository extends JpaRepository<OrganizationUnit, Long> {
}
