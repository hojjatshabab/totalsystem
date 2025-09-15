package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeValueDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.AttributeValueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attribute-value")
@Tag(name = "مقدار ویژگی ها", description = "مقادیر ویژگی های ارگان ها")
public class AttributeValueController extends BaseController {

    private final AttributeValueService attributeValueService;

    @PostMapping
    public ActionResult<AttributeValueDto> save(@RequestBody @Valid AttributeValueDto dto, Locale locale) {
        return RESULT(attributeValueService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<AttributeValueDto> update(@RequestBody @Valid AttributeValueDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        attributeValueService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("AttributeValue با این id یافت نشد: " + dto.getId()));
        return RESULT(attributeValueService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<AttributeValueDto>> findAll(@RequestParam int currentPage,
                                                                 @RequestParam int pageSize,
                                                                 Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<AttributeValueDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<AttributeValueDto> response = attributeValueService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/find-by-attribute-period")
    public ActionResult<PageResponse<AttributeValueDto>> findAll(@RequestParam int currentPage,
                                                                 @RequestParam int pageSize,
                                                                 @RequestParam UUID attributePeriodId,
                                                                 Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<AttributeValueDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<AttributeValueDto> response = attributeValueService.findByAttributePeriodId(attributePeriodId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<AttributeValueDto>> list(Locale locale) {
        List<AttributeValueDto> values = attributeValueService.findAll();
        return RESULT(values, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<AttributeValueDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        AttributeValueDto dto = attributeValueService.findById(id)
                .orElseThrow(() -> new ServiceException("AttributeValue با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        attributeValueService.findById(id)
                .orElseThrow(() -> new ServiceException("AttributeValue با این id یافت نشد: " + id));
        attributeValueService.deleteById(id);
        return RESULT(true, locale);
    }
}
