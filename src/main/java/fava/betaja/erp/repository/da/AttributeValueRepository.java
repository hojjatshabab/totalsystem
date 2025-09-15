package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.AttributeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, UUID> {
    Page<AttributeValue> findByAttributePeriodId(UUID attributePeriodId, Pageable pageable);
    @Query("SELECT COALESCE(SUM(av.value), 0) FROM AttributeValue av WHERE av.attributePeriod.id = :attributePeriodId")
    BigDecimal sumValueByAttributePeriodId(UUID attributePeriodId);
}
