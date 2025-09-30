package fava.betaja.erp.controller.security;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.PermissionDto;
import fava.betaja.erp.dto.security.RoleDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.security.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/role")
@Tag(name = "نقش ها", description = "مدیریت نقش ها")
public class RoleController extends BaseController {

    private final RoleService roleService;

    @PostMapping
    public ActionResult<RoleDto> save(@RequestBody @Valid RoleDto dto, Locale locale) {
        return RESULT(roleService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<RoleDto> update(@RequestBody @Valid RoleDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        roleService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("Role با این id یافت نشد: " + dto.getId()));
        return RESULT(roleService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<RoleDto>> findAll(@RequestParam int currentPage,
                                                       @RequestParam int pageSize,
                                                       Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<RoleDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<RoleDto> response = roleService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<RoleDto>> list(Locale locale) {
        List<RoleDto> dtoList = roleService.findAll();
        return RESULT(dtoList, locale);
    }

    @GetMapping("/name/{name}")
    public ActionResult<RoleDto> findByName(@PathVariable String name, Locale locale) {
        if (name == null || name.isBlank()) {
            return NO_CONTENT("id", locale);
        }
        RoleDto dto = roleService.findByName(name)
                .orElseThrow(() -> new ServiceException("Role با این id یافت نشد: " + name));
        return RESULT(dto, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<RoleDto> findById(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        RoleDto dto = roleService.findById(id)
                .orElseThrow(() -> new ServiceException("Role= با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        roleService.findById(id)
                .orElseThrow(() -> new ServiceException("Role با این id یافت نشد: " + id));
        roleService.deleteById(id);
        return RESULT(true, locale);
    }
}
