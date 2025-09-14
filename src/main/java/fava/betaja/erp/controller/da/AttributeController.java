package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.AttributeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attribute")
@Tag(name = "ویژگی ها", description = "ویژگی های ارگان ها")
public class AttributeController extends BaseController {

    private final AttributeService attributeService;

    @PostMapping
    public ActionResult<AttributeDto> save(@RequestBody @Valid AttributeDto attributeDto, Locale locale) {
        return RESULT(attributeService.save(attributeDto), locale);
    }

    @PutMapping
    public ActionResult<AttributeDto> update(@RequestBody @Valid AttributeDto attributeDto, Locale locale) {
        if (attributeDto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        attributeService.findById(attributeDto.getId())
                .orElseThrow(() -> new ServiceException("Attribute با این id یافت نشد: " + attributeDto.getId()));
        return RESULT(attributeService.update(attributeDto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<AttributeDto>> findAll(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<AttributeDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<AttributeDto> response = attributeService.findAll(request);
        return response.getRows().isEmpty() ? NO_CONTENT("attributes", locale) : RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<AttributeDto>> list(Locale locale) {
        List<AttributeDto> attributes = attributeService.findAll();
        return attributes.isEmpty() ? NO_CONTENT("attributes", locale) : RESULT(attributes, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<AttributeDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        AttributeDto dto = attributeService.findById(id)
                .orElseThrow(() -> new ServiceException("Attribute با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        attributeService.findById(id)
                .orElseThrow(() -> new ServiceException("Attribute با این id یافت نشد: " + id));
        attributeService.deleteById(id);
        return RESULT(true, locale);
    }
}
