package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.enums.ModeType;
import fava.betaja.erp.mapper.da.AttributeDtoMapper;
import fava.betaja.erp.service.da.AttributeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attribute")
@Tag(name = "ویژگی ها", description = "ویژگی های ارگان ها")
public class AttributeController extends BaseController {

    private final AttributeService attributeService;

    @PostMapping
    public ActionResult<AttributeDto> save(@RequestBody AttributeDto attributeDto, Locale locale) {
        isExist(attributeDto, ModeType.CREATE, locale);
        try {
            return RESULT(attributeService.save(attributeDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @PutMapping
    public ActionResult<AttributeDto> update(@RequestBody AttributeDto attributeDto, Locale locale) {
        isExist(attributeDto, ModeType.EDIT, locale);
        try {
            return RESULT(attributeService.update(attributeDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping
    public ActionResult<PageResponse<AttributeDto>> findAll(@RequestParam int currentPage, @RequestParam int pageSize, Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        PageRequest<AttributeDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<AttributeDto> attributeDtoPageResponse;
        try {
            attributeDtoPageResponse = attributeService.findAll(request);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (Objects.isNull(attributeDtoPageResponse)) {
            return NO_CONTENT("attributeDtoPageResponse", locale);
        } else {
            return RESULT(attributeDtoPageResponse, locale);
        }
    }

    @GetMapping("/list")
    public ActionResult<List<AttributeDto>> list(Locale locale) {
        try {
            return RESULT(attributeService.findAll(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/id/{id}")
    public ActionResult<Optional<AttributeDto>> findById(@PathVariable UUID id, Locale locale) {
        Optional<AttributeDto> optionalAttributeDto;
        if (id.equals(null)) {
            return NO_CONTENT(" id= " + id, locale);
        }
        try {
            optionalAttributeDto = attributeService.findById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalAttributeDto.isPresent()) {
            return RESULT(optionalAttributeDto, locale);
        } else {
            return NOT_FOUND(" id= " + id, locale);
        }
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!attributeService.findById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        try {
            return RESULT(attributeService.deleteById(id), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    private void isExist(AttributeDto attributeDto, ModeType modeType, Locale locale) {
        if (modeType.equals(ModeType.EDIT)) {
            if (Objects.nonNull(attributeDto.getId())) {
                Optional<AttributeDto> optionalAttributeDto = attributeService.findById(attributeDto.getId());
                if (!optionalAttributeDto.isPresent()) {
                    NOT_FOUND(" id ", locale);
                }
            } else {
                NO_CONTENT(" id ", locale);
            }
        }
        if (Objects.isNull(attributeDto.getName()) || attributeDto.getName().isEmpty()) {
            NO_CONTENT(" name ", locale);
        }
    }
}
