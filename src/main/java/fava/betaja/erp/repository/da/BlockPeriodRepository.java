package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.BlockPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlockPeriodRepository extends JpaRepository<BlockPeriod, UUID> {
    Page<BlockPeriod> findByBlockId(UUID blockId, Pageable pageable);

    Page<BlockPeriod> findByBlockIdAndPeriodRangeIdAndYear(UUID blockId, UUID periodId, String year, Pageable pageable);

    Page<BlockPeriod> findByPeriodRangeIdAndYear(UUID periodId, String year, Pageable pageable);
}
