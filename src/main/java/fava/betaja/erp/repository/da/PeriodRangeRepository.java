package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.PeriodRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PeriodRangeRepository extends JpaRepository<PeriodRange, UUID> {
}
