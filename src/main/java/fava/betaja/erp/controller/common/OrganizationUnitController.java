package fava.betaja.erp.controller.common;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.enums.ModeType;
import fava.betaja.erp.service.common.OrganizationUnitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/organization")
@RequiredArgsConstructor
@Tag(name = "یگان ها", description = "یگان ها")
public class OrganizationUnitController extends BaseController {

    private final OrganizationUnitService organizationUnitService;

    @PostMapping
    public ActionResult<OrganizationUnitDto> save(@RequestBody OrganizationUnitDto organizationUnitDto, Locale locale) {
        isExist(organizationUnitDto, ModeType.CREATE, locale);
        try {
            return RESULT(organizationUnitService.update(organizationUnitDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @PutMapping
    public ActionResult<OrganizationUnitDto> update(@RequestBody OrganizationUnitDto organizationUnitDto, Locale locale) {
        isExist(organizationUnitDto, ModeType.EDIT, locale);
        try {
            return RESULT(organizationUnitService.update(organizationUnitDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping
    public ActionResult<PageResponse<OrganizationUnitDto>> findAll(@RequestParam int currentPage
            , @RequestParam int pageSize, Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        fava.betaja.erp.dto.PageRequest<OrganizationUnitDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<OrganizationUnitDto> organizationUnitDtoPageResponse;
        try {
            organizationUnitDtoPageResponse = organizationUnitService.findAll(request);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (Objects.isNull(organizationUnitDtoPageResponse)) {
            return NO_CONTENT("organizationUnitDtoPageResponse", locale);
        } else {
            return RESULT(organizationUnitDtoPageResponse, locale);
        }
    }

    @GetMapping("/list")
    public ActionResult<List<OrganizationUnitDto>> list(Locale locale) {
        try {
            return RESULT(organizationUnitService.findAll(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/id/{id}")
    public ActionResult<Optional<OrganizationUnitDto>> findById(@PathVariable Long id, Locale locale) {
        Optional<OrganizationUnitDto> optionalOrganizationUnitDto;
        if (id.equals(null)) {
            return NO_CONTENT(" id= " + id, locale);
        }
        try {
            optionalOrganizationUnitDto = organizationUnitService.findById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalOrganizationUnitDto.isPresent()) {
            return RESULT(optionalOrganizationUnitDto, locale);
        } else {
            return NOT_FOUND(" id= " + id, locale);
        }
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!organizationUnitService.findById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        try {
            return RESULT(organizationUnitService.deleteById(id), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    private void isExist(OrganizationUnitDto organizationUnitDto, ModeType modeType, Locale locale) {
        if (modeType.equals(ModeType.EDIT)) {
            if (Objects.nonNull(organizationUnitDto.getId())) {
                Optional<OrganizationUnitDto> optionalOrganizationUnitDto = organizationUnitService.findById(organizationUnitDto.getId());
                if (!optionalOrganizationUnitDto.isPresent()) {
                    NOT_FOUND(" id ", locale);
                }
            } else {
                NO_CONTENT(" id ", locale);
            }
        }
        if (Objects.isNull(organizationUnitDto.getName()) || organizationUnitDto.getName().isEmpty()) {
            NO_CONTENT(" name ", locale);
        }
    }
}
