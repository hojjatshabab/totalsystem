package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.PlanDto;
import fava.betaja.erp.dto.da.ProjectDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {

    ProjectDto save(ProjectDto dto);

    ProjectDto update(ProjectDto dto);

    PageResponse<ProjectDto> findAll(PageRequest<ProjectDto> model);

    PageResponse<ProjectDto> findByPlanId(UUID planId, PageRequest<ProjectDto> model);

    List<ProjectDto> findAll();

    Optional<ProjectDto> findById(UUID id);

    void deleteById(UUID id);

}
