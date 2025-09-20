package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.ProgressPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgressPeriodRepository extends JpaRepository<ProgressPeriod, UUID> {
    Page<ProgressPeriod> findByReferenceId(UUID referenceId, Pageable pageable);

}
