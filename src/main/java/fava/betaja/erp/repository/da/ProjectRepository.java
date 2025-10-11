package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.Project;
import fava.betaja.erp.repository.da.extra.ProjectExtraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> , ProjectExtraRepository {
    Page<Project> findByPlanId(UUID planId, Pageable pageable);

    List<Project> findByPlanId(UUID planId);
}
