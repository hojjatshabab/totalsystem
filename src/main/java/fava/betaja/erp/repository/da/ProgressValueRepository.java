package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.ProgressValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProgressValueRepository extends JpaRepository<ProgressValue, UUID> {
    Page<ProgressValue> findByProgressPeriodId(UUID progressPeriodId, Pageable pageable);
    List<ProgressValue> findByProgressPeriodId(UUID progressPeriodId);
    @Query("SELECT COALESCE(SUM(av.value), 0) FROM ProgressValue av WHERE av.progressPeriod.id = :progressPeriodId")
    BigDecimal sumValueByProgressPeriodId(UUID progressPeriodId);
}
