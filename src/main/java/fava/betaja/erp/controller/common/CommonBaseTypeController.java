package fava.betaja.erp.controller.common;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.CommonBaseTypeDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.common.CommonBaseTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/common-base-type")
@Tag(name = "نوع داده‌های پایه مشترک", description = "مدیریت نوع داده‌های پایه مشترک")
public class CommonBaseTypeController extends BaseController {

    private final CommonBaseTypeService service;

    @PostMapping
    public ActionResult<CommonBaseTypeDto> save(@RequestBody @Valid CommonBaseTypeDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<CommonBaseTypeDto> update(@RequestBody @Valid CommonBaseTypeDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("CommonBaseType با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<CommonBaseTypeDto>> findAll(@RequestParam int currentPage,
                                                                 @RequestParam int pageSize,
                                                                 Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<CommonBaseTypeDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<CommonBaseTypeDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/search-or")
    public ActionResult<PageResponse<CommonBaseTypeDto>> findByClassNameContainingOrTitleContaining(
            @RequestParam String className,
            @RequestParam String title,
            @RequestParam int currentPage,
            @RequestParam int pageSize,
            Locale locale) {

        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }

        PageRequest<CommonBaseTypeDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<CommonBaseTypeDto> response = service.findByClassNameContainingOrTitleContaining(className, title, request);
        return RESULT(response, locale);
    }

    @GetMapping("/search-and")
    public ActionResult<PageResponse<CommonBaseTypeDto>> findByClassNameContainingAndTitleContaining(
            @RequestParam String className,
            @RequestParam String title,
            @RequestParam int currentPage,
            @RequestParam int pageSize,
            Locale locale) {

        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }

        PageRequest<CommonBaseTypeDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<CommonBaseTypeDto> response = service.findByClassNameContainingAndTitleContaining(className, title, request);
        return RESULT(response, locale);
    }

    @GetMapping("/title-contains")
    public ActionResult<List<CommonBaseTypeDto>> findByTitleContains(@RequestParam String title, Locale locale) {
        List<CommonBaseTypeDto> result = service.findByTitleContains(title);
        return RESULT(result, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<CommonBaseTypeDto>> list(Locale locale) {
        List<CommonBaseTypeDto> result = service.findAll();
        return RESULT(result, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<CommonBaseTypeDto> findById(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        CommonBaseTypeDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("CommonBaseType با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @GetMapping("/class-name/{className}")
    public ActionResult<CommonBaseTypeDto> findByClassName(@PathVariable String className, Locale locale) {
        CommonBaseTypeDto dto = service.findByClassName(className)
                .orElseThrow(() -> new ServiceException("CommonBaseType با این className یافت نشد: " + className));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("CommonBaseType با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
