package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.PlanDto;
import fava.betaja.erp.dto.da.ProjectDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/project")
@Tag(name = "پروژه ها", description = "مدیریت پروژه های هر طرح")
public class ProjectController extends BaseController {

    private final ProjectService service;

    @PostMapping
    public ActionResult<ProjectDto> save(@RequestBody @Valid ProjectDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<ProjectDto> update(@RequestBody @Valid ProjectDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("Project با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<ProjectDto>> findAll(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProjectDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProjectDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-plan")
    public ActionResult<PageResponse<ProjectDto>> findByPlanId(@RequestParam int currentPage,
                                                                             @RequestParam int pageSize,
                                                                             @RequestParam UUID planId,
                                                                             Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProjectDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProjectDto> response = service.findByPlanId(planId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<ProjectDto>> list(Locale locale) {
        List<ProjectDto> result = service.findAll();
        return RESULT(result, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<ProjectDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        ProjectDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("Project با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("Project با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
