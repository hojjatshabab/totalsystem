package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.ProjectPeriodDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.ProjectPeriodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/project-period")
@Tag(name = "دوره های بلوک", description = "مدیریت دورهای پروژه")
public class ProjectPeriodController extends BaseController {

    private final ProjectPeriodService service;

    @PostMapping
    public ActionResult<ProjectPeriodDto> save(@RequestBody @Valid ProjectPeriodDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<ProjectPeriodDto> update(@RequestBody @Valid ProjectPeriodDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("ProjectPeriod با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<ProjectPeriodDto>> findAll(@RequestParam int currentPage,
                                                                @RequestParam int pageSize,
                                                                Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProjectPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProjectPeriodDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-project")
    public ActionResult<PageResponse<ProjectPeriodDto>> findByProjectId(@RequestParam int currentPage,
                                                                      @RequestParam int pageSize,
                                                                      @RequestParam UUID projectId,
                                                                      Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProjectPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProjectPeriodDto> response = service.findByProjectId(projectId, request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-project-range-year")
    public ActionResult<PageResponse<ProjectPeriodDto>> findByBlockIdAndPeriodRangeIdAndYear(@RequestParam int currentPage,
                                                                                             @RequestParam int pageSize,
                                                                                             @RequestParam UUID projectId,
                                                                                             @RequestParam UUID periodRangeId,
                                                                                             @RequestParam String year,
                                                                                             Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProjectPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProjectPeriodDto> response = service.findByProjectIdAndPeriodRangeIdAndYear(projectId, periodRangeId, year, request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-range-year")
    public ActionResult<PageResponse<ProjectPeriodDto>> findByPeriodRangeIdAndYear(@RequestParam int currentPage,
                                                                                   @RequestParam int pageSize,
                                                                                   @RequestParam UUID periodRangeId,
                                                                                   @RequestParam String year,
                                                                                   Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProjectPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProjectPeriodDto> response = service.findByPeriodRangeIdAndYear(periodRangeId,year, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<ProjectPeriodDto>> list(Locale locale) {
        List<ProjectPeriodDto> result = service.findAll();
        return RESULT(result, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<ProjectPeriodDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        ProjectPeriodDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("ProjectPeriod با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @GetMapping("/find-by-project-active-true/{projectId}")
    public ActionResult<ProjectPeriodDto> findByProjectIdAndIsActiveTrue(@PathVariable UUID projectId, Locale locale) {
        if (projectId == null) {
            return NO_CONTENT("id", locale);
        }
        ProjectPeriodDto dto = service.findByProjectIdAndIsActiveTrue(projectId)
                .orElseThrow(() -> new ServiceException("ProjectPeriod با این id یافت نشد: " + projectId));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("ProjectPeriod با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
