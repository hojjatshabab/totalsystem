package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.ProgressPeriodDto;
import fava.betaja.erp.dto.da.AttributePeriodProgressDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.AttributePeriodReportService;
import fava.betaja.erp.service.da.ProgressPeriodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/progress-period")
@Tag(name = "اطلاعات زمانی دوره پیشرفت", description = "اطلاعات زمانی دوره پیشرفت")
public class AttributePeriodController extends BaseController {

    private final ProgressPeriodService progressPeriodService;
    private final AttributePeriodReportService attributePeriodReportService;

    @PostMapping
    public ActionResult<ProgressPeriodDto> save(@RequestBody @Valid ProgressPeriodDto dto, Locale locale) {
        return RESULT(progressPeriodService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<ProgressPeriodDto> update(@RequestBody @Valid ProgressPeriodDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        progressPeriodService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("ProgressPeriod با این id یافت نشد: " + dto.getId()));
        return RESULT(progressPeriodService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<ProgressPeriodDto>> findAll(@RequestParam int currentPage,
                                                                 @RequestParam int pageSize,
                                                                 Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProgressPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProgressPeriodDto> response = progressPeriodService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/find-by-reference")
    public ActionResult<PageResponse<ProgressPeriodDto>> findByReferenceId(@RequestParam int currentPage,
                                                                           @RequestParam int pageSize,
                                                                           @RequestParam UUID referenceId,
                                                                           Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<ProgressPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<ProgressPeriodDto> response = progressPeriodService.findByReferenceId(referenceId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<ProgressPeriodDto>> list(Locale locale) {
        List<ProgressPeriodDto> periods = progressPeriodService.findAll();
        return RESULT(periods, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<ProgressPeriodDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        ProgressPeriodDto dto = progressPeriodService.findById(id)
                .orElseThrow(() -> new ServiceException("ProgressPeriod با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }
    @GetMapping("/{id}/total-value")
    public ActionResult<BigDecimal> getTotalValue(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        BigDecimal totalValue = progressPeriodService.getTotalValue(id);
        return RESULT(totalValue, locale);
    }

    @GetMapping("/{id}/progress")
    public ActionResult<AttributePeriodProgressDto> getProgress(@PathVariable UUID id, Locale locale) {
        try {
            return RESULT(attributePeriodReportService.getProgress(id), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        progressPeriodService.findById(id)
                .orElseThrow(() -> new ServiceException("ProgressPeriod با این id یافت نشد: " + id));
        progressPeriodService.deleteById(id);
        return RESULT(true, locale);
    }
}
