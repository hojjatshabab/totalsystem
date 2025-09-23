package fava.betaja.erp.controller.common;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.CommonBaseDataDto;
import fava.betaja.erp.entities.common.CommonBaseData;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.common.CommonBaseDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/common-base-data")
@Tag(name = "داده‌های پایه مشترک", description = "مدیریت داده‌های پایه مشترک")
public class CommonBaseDataController extends BaseController {

    private final CommonBaseDataService service;

    @PostMapping
    public ActionResult<CommonBaseDataDto> save(@RequestBody @Valid CommonBaseDataDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<CommonBaseDataDto> update(@RequestBody @Valid CommonBaseDataDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("CommonBaseData با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<CommonBaseDataDto>> findAll(@RequestParam int currentPage,
                                                                 @RequestParam int pageSize,
                                                                 Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<CommonBaseDataDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<CommonBaseDataDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/by-type-active")
    public ActionResult<PageResponse<CommonBaseData>> findByCommonBaseTypeIdAndActiveTrue(@RequestParam Long typeId,
                                                                                          @RequestParam int currentPage,
                                                                                          @RequestParam int pageSize,
                                                                                          Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<CommonBaseDataDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<CommonBaseData> response = service.findByCommonBaseTypeIdAndActiveTrue(typeId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/by-type-order")
    public ActionResult<List<CommonBaseData>> findByCommonBaseTypeIdOrderByOrderNoAsc(@RequestParam Long typeId,
                                                                                      Locale locale) {
        List<CommonBaseData> result = service.findByCommonBaseTypeIdOrderByOrderNoAsc(typeId);
        return RESULT(result, locale);
    }

    @GetMapping("/by-type-active-list")
    public ActionResult<List<CommonBaseData>> findByCommonBaseTypeIdAndActiveTrue(@RequestParam Long typeId,
                                                                                  Locale locale) {
        List<CommonBaseData> result = service.findByCommonBaseTypeIdAndActiveTrue(typeId);
        return RESULT(result, locale);
    }

    @GetMapping("/search")
    public ActionResult<PageResponse<CommonBaseData>> findByValueContainsAndCommonBaseTypeIdAndActiveTrue(
            @RequestParam String value,
            @RequestParam Long typeId,
            @RequestParam int currentPage,
            @RequestParam int pageSize,
            Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<CommonBaseDataDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<CommonBaseData> response = service.findByValueContainsAndCommonBaseTypeIdAndActiveTrue(value, typeId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/by-type-value")
    public ActionResult<List<CommonBaseDataDto>> findByCommonBaseTypeAndValueContain(
            @RequestParam Long typeId,
            @RequestParam String value,
            Locale locale) {
        List<CommonBaseDataDto> result = service.findByCommonBaseTypeAndValueContain(typeId, value);
        return RESULT(result, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<CommonBaseDataDto>> list(Locale locale) {
        List<CommonBaseDataDto> result = service.findAll();
        return RESULT(result, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<CommonBaseDataDto> findById(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        CommonBaseDataDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("CommonBaseData با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("CommonBaseData با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
