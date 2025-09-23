package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.PlanDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlanService {

    PlanDto save(PlanDto dto);

    PlanDto update(PlanDto dto);

    PageResponse<PlanDto> findAll(PageRequest<PlanDto> model);

    PageResponse<PlanDto> findByOrganizationUnitId(Long organizationUnitId, PageRequest<PlanDto> model);

    List<PlanDto> findAll();

    Optional<PlanDto> findById(UUID id);

    void deleteById(UUID id);

}
