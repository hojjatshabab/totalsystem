package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.PeriodRangeDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.PeriodRangeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/period-range")
@Tag(name = "دوره های زمانی", description = "دوره های زمانی")
public class PeriodRangeController extends BaseController {

    private final PeriodRangeService periodRangeService;

    @PostMapping
    public ActionResult<PeriodRangeDto> save(@RequestBody @Valid PeriodRangeDto dto, Locale locale) {
        return RESULT(periodRangeService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<PeriodRangeDto> update(@RequestBody @Valid PeriodRangeDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        periodRangeService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("PeriodRange با این id یافت نشد: " + dto.getId()));
        return RESULT(periodRangeService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<PeriodRangeDto>> findAll(@RequestParam int currentPage,
                                                              @RequestParam int pageSize,
                                                              Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<PeriodRangeDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<PeriodRangeDto> response = periodRangeService.findAll(request);
        return response.getRows().isEmpty() ? NO_CONTENT("periodRanges", locale) : RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<PeriodRangeDto>> list(Locale locale) {
        List<PeriodRangeDto> periods = periodRangeService.findAll();
        return periods.isEmpty() ? NO_CONTENT("periodRanges", locale) : RESULT(periods, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<PeriodRangeDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        PeriodRangeDto dto = periodRangeService.findById(id)
                .orElseThrow(() -> new ServiceException("PeriodRange با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        periodRangeService.findById(id)
                .orElseThrow(() -> new ServiceException("PeriodRange با این id یافت نشد: " + id));
        periodRangeService.deleteById(id);
        return RESULT(true, locale);
    }
}
