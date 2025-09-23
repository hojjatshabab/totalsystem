package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.PlanDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.PlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/plan")
@Tag(name = "طرح ها", description = "مدیریت طرح های شرکت ها و موسسات")
public class PlanController extends BaseController {

    private final PlanService service;

    @PostMapping
    public ActionResult<PlanDto> save(@RequestBody @Valid PlanDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<PlanDto> update(@RequestBody @Valid PlanDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("Plan با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<PlanDto>> findAll(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<PlanDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<PlanDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-org")
    public ActionResult<PageResponse<PlanDto>> findByOrganizationUnitId(@RequestParam int currentPage,
                                                                             @RequestParam int pageSize,
                                                                             @RequestParam Long organizationUnitId,
                                                                             Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<PlanDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<PlanDto> response = service.findByOrganizationUnitId(organizationUnitId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<PlanDto>> list(Locale locale) {
        List<PlanDto> result = service.findAll();
        return RESULT(result, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<PlanDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        PlanDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("Plan با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("Plan با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
