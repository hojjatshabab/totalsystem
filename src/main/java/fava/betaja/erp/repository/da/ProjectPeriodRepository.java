package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.ProjectPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectPeriodRepository extends JpaRepository<ProjectPeriod, UUID> {
    Page<ProjectPeriod> findByProjectId(UUID projectId, Pageable pageable);
    Optional<ProjectPeriod> findByProjectIdAndIsActiveTrue(UUID projectId);

    Page<ProjectPeriod> findByProjectIdAndPeriodRangeIdAndYear(UUID projectId, UUID periodId, String year, Pageable pageable);

    Page<ProjectPeriod> findByPeriodRangeIdAndYear(UUID periodId, String year, Pageable pageable);
}
