package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    Page<Attribute> findByOrganizationUnitId(Long organizationUnitId, Pageable pageable);
}
