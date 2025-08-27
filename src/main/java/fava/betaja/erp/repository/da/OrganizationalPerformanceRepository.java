package fava.betaja.erp.repository.da;


import fava.betaja.erp.entities.da.OrganizationalPerformance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationalPerformanceRepository extends JpaRepository<OrganizationalPerformance, UUID> {


    Page<OrganizationalPerformance> findAll(Pageable pageable);

    OrganizationalPerformance save(OrganizationalPerformance organizationalPerformance);


}
