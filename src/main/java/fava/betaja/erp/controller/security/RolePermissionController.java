package fava.betaja.erp.controller.security;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.RolePermissionDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.security.RolePermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/role-permission")
@Tag(name = "مجوز هر نقش", description = "مدیریت مجوز هر نقش")
public class RolePermissionController extends BaseController {

    private final RolePermissionService service;

    @PostMapping
    public ActionResult<RolePermissionDto> save(@RequestBody @Valid RolePermissionDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<RolePermissionDto> update(@RequestBody @Valid RolePermissionDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("RolePermission با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<RolePermissionDto>> findAll(@RequestParam int currentPage,
                                                                 @RequestParam int pageSize,
                                                                 Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<RolePermissionDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<RolePermissionDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<RolePermissionDto>> list(Locale locale) {
        List<RolePermissionDto> dtoList = service.findAll();
        return RESULT(dtoList, locale);
    }

    @GetMapping("/find-by-role-permission")
    public ActionResult<List<RolePermissionDto>> findByRoleIdAndPermissionId(@RequestParam Long roleId, @RequestParam Long permissionId, Locale locale) {
        List<RolePermissionDto> dtoList = service.findByRoleIdAndPermissionId(roleId, permissionId);
        return RESULT(dtoList, locale);
    }

    @GetMapping("/find-by-permission")
    public ActionResult<List<RolePermissionDto>> findByPermissionId(@RequestParam Long permissionId, Locale locale) {
        List<RolePermissionDto> dtoList = service.findByPermissionId(permissionId);
        return RESULT(dtoList, locale);
    }

    @GetMapping("/find-by-role")
    public ActionResult<List<RolePermissionDto>> findByRoleId(@RequestParam Long roleId, Locale locale) {
        List<RolePermissionDto> dtoList = service.findByRoleId(roleId);
        return RESULT(dtoList, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<RolePermissionDto> findById(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        RolePermissionDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("RolePermission= با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("RolePermission با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
