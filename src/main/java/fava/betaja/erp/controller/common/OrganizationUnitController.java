package fava.betaja.erp.controller.common;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.common.OrganizationUnitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/organization")
@RequiredArgsConstructor
@Tag(name = "یگان ها", description = "مدیریت یگان ها")
public class OrganizationUnitController extends BaseController {

    private final OrganizationUnitService organizationUnitService;

    @PostMapping
    public ActionResult<OrganizationUnitDto> save(@RequestBody OrganizationUnitDto dto, Locale locale) {
        try {
            return RESULT(organizationUnitService.save(dto), locale);
        } catch (ServiceException e) {
            return BAD_REQUEST(e.getMessage(), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }

    @GetMapping("/current-organization")
    public ActionResult<OrganizationUnitDto> getCurrentOrganization(Locale locale) {
        try {
            return RESULT(organizationUnitService.getCurrentOrganizationUnit(), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }

    @PutMapping
    public ActionResult<OrganizationUnitDto> update(@RequestBody OrganizationUnitDto dto, Locale locale) {
        try {
            return RESULT(organizationUnitService.update(dto), locale);
        } catch (ServiceException e) {
            return BAD_REQUEST(e.getMessage(), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }

    @GetMapping
    public ActionResult<PageResponse<OrganizationUnitDto>> findAll(@RequestParam int currentPage,
                                                                   @RequestParam int pageSize, Locale locale) {
        try {
            PageRequest<OrganizationUnitDto> request = new PageRequest<>();
            request.setPageSize(pageSize);
            request.setCurrentPage(currentPage);
            return RESULT(organizationUnitService.findAll(request), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }

    @GetMapping("/list")
    public ActionResult<List<OrganizationUnitDto>> list(Locale locale) {
        try {
            return RESULT(organizationUnitService.findAll(), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }

    @GetMapping("/id/{id}")
    public ActionResult<OrganizationUnitDto> findById(@PathVariable Long id, Locale locale) {
        try {
            return RESULT(organizationUnitService.findById(id).orElseThrow(() ->
                    new ServiceException("یگان مورد نظر پیدا نشد.")), locale);
        } catch (ServiceException e) {
            return BAD_REQUEST(e.getMessage(), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        try {
            return RESULT(organizationUnitService.deleteById(id), locale);
        } catch (ServiceException e) {
            return BAD_REQUEST(e.getMessage(), locale);
        } catch (Exception e) {
            return INTERNAL_SERVER_ERROR(e.getMessage(), locale);
        }
    }
}
