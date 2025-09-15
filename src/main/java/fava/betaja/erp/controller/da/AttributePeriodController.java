package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributePeriodDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.AttributePeriodService;
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
@RequestMapping("api/attribute-period")
@Tag(name = "دیتای دوره زمانی", description = "دیتای هر دوره زمانی")
public class AttributePeriodController extends BaseController {

    private final AttributePeriodService attributePeriodService;

    @PostMapping
    public ActionResult<AttributePeriodDto> save(@RequestBody @Valid AttributePeriodDto dto, Locale locale) {
        return RESULT(attributePeriodService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<AttributePeriodDto> update(@RequestBody @Valid AttributePeriodDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        attributePeriodService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("AttributePeriod با این id یافت نشد: " + dto.getId()));
        return RESULT(attributePeriodService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<AttributePeriodDto>> findAll(@RequestParam int currentPage,
                                                                  @RequestParam int pageSize,
                                                                  Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<AttributePeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<AttributePeriodDto> response = attributePeriodService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/find-by-attribute")
    public ActionResult<PageResponse<AttributePeriodDto>> findByAttributeId(@RequestParam int currentPage,
                                                                            @RequestParam int pageSize,
                                                                            @RequestParam UUID attributeId,
                                                                            Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<AttributePeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<AttributePeriodDto> response = attributePeriodService.findByAttributeId(attributeId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<AttributePeriodDto>> list(Locale locale) {
        List<AttributePeriodDto> periods = attributePeriodService.findAll();
        return RESULT(periods, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<AttributePeriodDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        AttributePeriodDto dto = attributePeriodService.findById(id)
                .orElseThrow(() -> new ServiceException("AttributePeriod با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }
    @GetMapping("/{id}/total-value")
    public ActionResult<BigDecimal> getTotalValue(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        BigDecimal totalValue = attributePeriodService.getTotalValue(id);
        return RESULT(totalValue, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        attributePeriodService.findById(id)
                .orElseThrow(() -> new ServiceException("AttributePeriod با این id یافت نشد: " + id));
        attributePeriodService.deleteById(id);
        return RESULT(true, locale);
    }
}
