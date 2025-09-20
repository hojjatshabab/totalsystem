package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.ProgressValueDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.ProgressValueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/progress-value")
@Tag(name = "مقدار اطلاعات دوره پیشرفت", description = "مقدار اطلاعات دوره پیشرفت")
public class ProgressValueController extends BaseController {

    private final ProgressValueService progressValueService;

    @PostMapping
    public ActionResult<ProgressValueDto> save(@RequestBody @Valid ProgressValueDto dto, Locale locale) {
        return RESULT(progressValueService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<ProgressValueDto> update(@RequestBody @Valid ProgressValueDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        progressValueService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("ProgressValue با این id یافت نشد: " + dto.getId()));
        return RESULT(progressValueService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<ProgressValueDto>> findAll(@RequestParam int currentPage,
                                                                @RequestParam int pageSize,
                                                                Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProgressValueDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProgressValueDto> response = progressValueService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/find-by-progress-period")
    public ActionResult<PageResponse<ProgressValueDto>> findAll(@RequestParam int currentPage,
                                                                @RequestParam int pageSize,
                                                                @RequestParam UUID attributePeriodId,
                                                                Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProgressValueDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProgressValueDto> response = progressValueService.findByProgressValueId(attributePeriodId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<ProgressValueDto>> list(Locale locale) {
        List<ProgressValueDto> values = progressValueService.findAll();
        return RESULT(values, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<ProgressValueDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        ProgressValueDto dto = progressValueService.findById(id)
                .orElseThrow(() -> new ServiceException("ProgressValue با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        progressValueService.findById(id)
                .orElseThrow(() -> new ServiceException("ProgressValue با این id یافت نشد: " + id));
        progressValueService.deleteById(id);
        return RESULT(true, locale);
    }
}
