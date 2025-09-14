package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.AttributePeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttributePeriodRepository extends JpaRepository<AttributePeriod, UUID> {

    Page<AttributePeriod> findByAttributeId(UUID attributeId, Pageable pageable);

}
