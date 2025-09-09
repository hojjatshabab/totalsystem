package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.AttributeValueDto;
import fava.betaja.erp.enums.ModeType;
import fava.betaja.erp.service.da.AttributeValueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attribute-value")
@Tag(name = "مقدار ویژگی ها", description = " مقادیر ویژگی های ارگان ها")
public class AttributeValueController extends BaseController {

    private final AttributeValueService attributeValueService;

    @PostMapping
    public ActionResult<AttributeValueDto> save(@RequestBody AttributeValueDto attributeValueDto, Locale locale) {
        isExist(attributeValueDto, ModeType.CREATE, locale);
        try {
            return RESULT(attributeValueService.update(attributeValueDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @PutMapping
    public ActionResult<AttributeValueDto> update(@RequestBody AttributeValueDto attributeValueDto, Locale locale) {
        isExist(attributeValueDto, ModeType.EDIT, locale);
        try {
            return RESULT(attributeValueService.update(attributeValueDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping
    public ActionResult<PageResponse<AttributeValueDto>> findAll(@RequestParam int currentPage, @RequestParam int pageSize, Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        PageRequest<AttributeValueDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<AttributeValueDto> attributeValueDtoPageResponse;
        try {
            attributeValueDtoPageResponse = attributeValueService.findAll(request);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (Objects.isNull(attributeValueDtoPageResponse)) {
            return NO_CONTENT("attributeValueDtoPageResponse", locale);
        } else {
            return RESULT(attributeValueDtoPageResponse, locale);
        }
    }

    @GetMapping("/list")
    public ActionResult<List<AttributeValueDto>> list(Locale locale) {
        try {
            return RESULT(attributeValueService.findAll(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/id/{id}")
    public ActionResult<Optional<AttributeValueDto>> findById(@PathVariable UUID id, Locale locale) {
        Optional<AttributeValueDto> attributeValueDto;
        if (id.equals(null)) {
            return NO_CONTENT(" id= " + id, locale);
        }
        try {
            attributeValueDto = attributeValueService.findById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (attributeValueDto.isPresent()) {
            return RESULT(attributeValueDto, locale);
        } else {
            return NOT_FOUND(" id= " + id, locale);
        }
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!attributeValueService.findById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        try {
            return RESULT(attributeValueService.deleteById(id), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    private void isExist(AttributeValueDto attributeValueDto, ModeType modeType, Locale locale) {
        if (modeType.equals(ModeType.EDIT)) {
            if (Objects.nonNull(attributeValueDto.getId())) {
                Optional<AttributeValueDto> optionalAttributeDto = attributeValueService.findById(attributeValueDto.getId());
                if (!optionalAttributeDto.isPresent()) {
                    NOT_FOUND(" id ", locale);
                }
            } else {
                NO_CONTENT(" id ", locale);
            }
        }
        if (Objects.isNull(attributeValueDto.getValue())) {
            NO_CONTENT(" value ", locale);
        }
    }
}
