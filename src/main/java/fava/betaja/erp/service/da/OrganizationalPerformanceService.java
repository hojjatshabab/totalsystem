package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.da.OrganizationalPerformanceDto;
import fava.betaja.erp.dto.da.OrganizationalPerformanceInfoDto;

import fava.betaja.erp.entities.da.OrganizationalPerformance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationalPerformanceService {

    Page<OrganizationalPerformanceDto> findAll(Pageable pageable);

    OrganizationalPerformanceDto save(OrganizationalPerformanceDto organizationalPerformanceDto);

    public Optional<OrganizationalPerformanceDto> findById(UUID id);


    public Boolean deleteById(UUID id);

}
