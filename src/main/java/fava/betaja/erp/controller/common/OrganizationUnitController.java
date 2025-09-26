package fava.betaja.erp.controller.common;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.enums.ModeType;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.common.OrganizationUnitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/organization")
@RequiredArgsConstructor
@Tag(name = "یگان ها", description = "مدیریت یگان ها")
public class OrganizationUnitController extends BaseController {

    private final OrganizationUnitService organizationUnitService;

    @PostMapping
    public ActionResult<OrganizationUnitDto> save(@RequestBody OrganizationUnitDto organizationUnitDto, Locale locale) {
        isExist(organizationUnitDto, ModeType.CREATE, locale);
        try {
            return RESULT(organizationUnitService.save(organizationUnitDto), locale);
        } catch (DataIntegrityViolationException exception) {
            return CONFLICT("name", locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @PutMapping
    public ActionResult<OrganizationUnitDto> update(@RequestBody OrganizationUnitDto organizationUnitDto, Locale locale) {
        isExist(organizationUnitDto, ModeType.EDIT, locale);
        try {
            return RESULT(organizationUnitService.update(organizationUnitDto), locale);
        } catch (DataIntegrityViolationException exception) {
            return CONFLICT("value", locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/current-organization")
    public ActionResult<OrganizationUnitDto> getCurrentOrgLogin(Locale locale) {
        try {
            return RESULT(organizationUnitService.getCurrentOrganizationUnit(), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }

    @GetMapping("/root-organization")
    public ActionResult<OrganizationUnitDto> getRootOrganization(Locale locale) {
        try {
            return RESULT(organizationUnitService.findByParentIdIsNull(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/list")
    public ActionResult<List<OrganizationUnitDto>> findByAllList(Locale locale) {
        try {
            return RESULT(organizationUnitService.findAllList(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping
    public ActionResult<PageResponse<OrganizationUnitDto>> findAll(@RequestParam int currentPage,
                                                                   @RequestParam int pageSize,
                                                                   Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        PageRequest<OrganizationUnitDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<OrganizationUnitDto> organizationUnitDtoResponse;
        try {
            organizationUnitDtoResponse = organizationUnitService.findAll(request);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (Objects.isNull(organizationUnitDtoResponse)) {
            return NO_CONTENT("commonBaseDataDtoPageResponse", locale);
        } else {
            return RESULT(organizationUnitDtoResponse, locale);
        }

    }

    @GetMapping("/id/{id}")
    public ActionResult<Optional<OrganizationUnitDto>> findById(@PathVariable Long id, Locale locale) {
        Optional<OrganizationUnitDto> optionalOrganizationUnitDto;
        if (id == null) {
            return NO_CONTENT("id =" + id, locale);
        }
        try {
            optionalOrganizationUnitDto = organizationUnitService.findById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalOrganizationUnitDto.isPresent()) {
            return RESULT(optionalOrganizationUnitDto, locale);
        } else {
            return NOT_FOUND("commonBaseData", locale);
        }
    }

    @GetMapping("/find-all-children-by-code-path/{codePath}")
    public ActionResult<Optional<List<OrganizationUnitDto>>> findAllChildrenByCodePath(@PathVariable String codePath, Locale locale) {
        Optional<List<OrganizationUnitDto>> optionalOrganizationUnitDto;
        if (codePath == null || codePath.isEmpty()) {
            return NO_CONTENT("codePath =" + codePath, locale);
        }
        try {
            optionalOrganizationUnitDto = organizationUnitService.findAllChildrenByCodePath(codePath);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalOrganizationUnitDto.isPresent()) {
            return RESULT(optionalOrganizationUnitDto, locale);
        } else {
            return RESULT(Optional.empty(), locale);
        }
    }


    @GetMapping("/find-all-children-by-parent-id/{id}")
    public ActionResult<Optional<List<OrganizationUnitDto>>> findAllChildrenByParentId(@PathVariable Long id, Locale locale) {
        Optional<List<OrganizationUnitDto>> optionalOrganizationUnitDto;
        if (id == null) {
            return NO_CONTENT("id =" + id, locale);
        }
        try {
            optionalOrganizationUnitDto = organizationUnitService.findAllChildrenById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalOrganizationUnitDto.isPresent()) {
            return RESULT(optionalOrganizationUnitDto, locale);
        } else {
            return RESULT(Optional.empty(), locale);
        }
    }

    @GetMapping("/find-all-force-with-out-children")
    public ActionResult<List<OrganizationUnitDto>> findAllForceWithOutChildren(Locale locale) {
        try {
            return RESULT(organizationUnitService.findAllForceWithOutChildren(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/find-all-force-by-parent-with-out-children")
    public ActionResult<List<OrganizationUnitDto>> findAllForceByParentWithOutChildren(Locale locale) {
        try {
            return RESULT(organizationUnitService.findAllForceByParentWithOutChildren(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/find-children-by-parent-id/{id}")
    public ActionResult<Optional<List<OrganizationUnitDto>>> findChildrenByParentId(@PathVariable Long id, Locale locale) {
        Optional<List<OrganizationUnitDto>> optionalOrganizationUnitDto;
        if (id == null) {
            return NO_CONTENT("id =" + id, locale);
        }
        try {
            optionalOrganizationUnitDto = organizationUnitService.findChildrenById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalOrganizationUnitDto.isPresent()) {
            return RESULT(optionalOrganizationUnitDto, locale);
        } else {
            return RESULT(Optional.empty(), locale);
        }
    }

    @GetMapping("/find-children-by-parent-code-path/{codePath}")
    public ActionResult<Optional<List<OrganizationUnitDto>>> findChildrenByParentCodePath(@PathVariable String codePath, Locale locale) {
        Optional<List<OrganizationUnitDto>> optionalOrganizationUnitDto;
        if (codePath == null) {
            return NO_CONTENT("codePath =" + codePath, locale);
        }
        try {
            optionalOrganizationUnitDto = organizationUnitService.findChildrenByCodePath(codePath);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalOrganizationUnitDto.isPresent()) {
            return RESULT(optionalOrganizationUnitDto, locale);
        } else {
            return RESULT(Optional.empty(), locale);
        }
    }

    @GetMapping("/find-organization-by-code/{code}")
    public ActionResult<Optional<List<OrganizationUnitDto>>> findOrganizationByCode(@PathVariable String code, Locale locale) {
        Optional<List<OrganizationUnitDto>> optionalOrganizationUnitDto;
        if (code == null || code.isEmpty()) {
            return NO_CONTENT("code =" + code, locale);
        }
        try {
            optionalOrganizationUnitDto = organizationUnitService.findByCode(code);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalOrganizationUnitDto.isPresent()) {
            return RESULT(optionalOrganizationUnitDto, locale);
        } else {
            return RESULT(Optional.empty(), locale);
        }
    }

    @GetMapping("/name/{name}")
    public ActionResult<Optional<List<OrganizationUnitDto>>> findByNameContains(@PathVariable String name, Locale locale) {
        Optional<List<OrganizationUnitDto>> organizationUnitsDto;
        if (name.equals(null) || name.isEmpty() || name.isBlank()) {
            return NO_CONTENT("name =" + name, locale);
        }
        try {
            organizationUnitsDto = organizationUnitService.findByNameContains(name);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        return RESULT(organizationUnitsDto, locale);
    }

    @GetMapping("/complete-name/{name}")
    public ActionResult<Optional<List<OrganizationUnitDto>>> findByCompleteNameContains(@PathVariable String name, Locale locale) {
        Optional<List<OrganizationUnitDto>> organizationUnitsDto;
        if (name.equals(null) || name.isEmpty() || name.isBlank()) {
            return NO_CONTENT("name =" + name, locale);
        }
        try {
            organizationUnitsDto = organizationUnitService.findByNameContains(name);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        return RESULT(organizationUnitsDto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!organizationUnitService.findById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        if (organizationUnitService.findAllChildrenById(id).get().size() > 1) {
            return CONFLICT("id", locale);
        }
        try {
            return RESULT(organizationUnitService.deleteById(id), locale);
        } catch (DataIntegrityViolationException exception) {
            return CONFLICT("id is used", locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    private void isExist(OrganizationUnitDto organizationUnitDto, ModeType modeType, Locale locale) {
        if (modeType.equals(ModeType.EDIT)) {
            if (Objects.nonNull(organizationUnitDto.getId())) {
                Optional<OrganizationUnitDto> optionalOrganizationUnitDto =
                        organizationUnitService.findById(organizationUnitDto.getId());
                if (!optionalOrganizationUnitDto.isPresent()) {
                    NOT_FOUND("id", locale);
                }
            } else {
                NO_CONTENT("id", locale);
            }
        } else {
            String organizationName = organizationUnitDto.getName();
            if (Objects.nonNull(organizationName)) {

                Optional<OrganizationUnitDto> optionalOrganizationUnitDto =
                        organizationUnitService.findByName(organizationName);
                if (optionalOrganizationUnitDto.isPresent()) {
                    CONFLICT("organizationName", locale);
                }
            }

            if (Objects.nonNull(organizationUnitDto.getParentId())) {
                if (!organizationUnitService.findById(organizationUnitDto.getParentId()).isPresent())
                    NOT_FOUND("parentId", locale);
            } else {
                NO_CONTENT("parentId", locale);
            }

        }
    }
}
