package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.ProjectPeriodDto;
import fava.betaja.erp.entities.da.ProjectPeriod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectPeriodService {

    ProjectPeriodDto save(ProjectPeriodDto dto);

    ProjectPeriodDto update(ProjectPeriodDto dto);

    Optional<ProjectPeriodDto> findByProjectIdAndIsActiveTrue(UUID projectId);

    PageResponse<ProjectPeriodDto> findAll(PageRequest<ProjectPeriodDto> model);

    PageResponse<ProjectPeriodDto> findByProjectId(UUID projectId, PageRequest<ProjectPeriodDto> model);

    PageResponse<ProjectPeriodDto> findByProjectIdAndPeriodRangeIdAndYear(UUID projectId, UUID periodId, String year, PageRequest<ProjectPeriodDto> model);

    PageResponse<ProjectPeriodDto> findByPeriodRangeIdAndYear(UUID periodId, String year, PageRequest<ProjectPeriodDto> model);

    List<ProjectPeriodDto> findAll();

    Optional<ProjectPeriodDto> findById(UUID id);

    void deleteById(UUID id);

}
