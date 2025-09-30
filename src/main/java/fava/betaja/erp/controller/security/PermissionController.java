package fava.betaja.erp.controller.security;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.PermissionDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.security.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/permission")
@Tag(name = "مجوز ها", description = "مدیریت مجوز ها")
public class PermissionController extends BaseController {

    private final PermissionService permissionService;

    @PostMapping
    public ActionResult<PermissionDto> save(@RequestBody @Valid PermissionDto permissionDto, Locale locale) {
        return RESULT(permissionService.save(permissionDto), locale);
    }

    @PutMapping
    public ActionResult<PermissionDto> update(@RequestBody @Valid PermissionDto permissionDto, Locale locale) {
        if (permissionDto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        permissionService.findById(permissionDto.getId())
                .orElseThrow(() -> new ServiceException("Permission با این id یافت نشد: " + permissionDto.getId()));
        return RESULT(permissionService.update(permissionDto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<PermissionDto>> findAll(@RequestParam int currentPage,
                                                             @RequestParam int pageSize,
                                                             Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<PermissionDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<PermissionDto> response = permissionService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<PermissionDto>> list(Locale locale) {
        List<PermissionDto> permissionDtos = permissionService.findAll();
        return RESULT(permissionDtos, locale);
    }

    @GetMapping("/name/{name}")
    public ActionResult<PermissionDto> findByName(@PathVariable String name, Locale locale) {
        if (name == null || name.isBlank()) {
            return NO_CONTENT("id", locale);
        }
        PermissionDto dto = permissionService.findByName(name)
                .orElseThrow(() -> new ServiceException("Permission با این id یافت نشد: " + name));
        return RESULT(dto, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<PermissionDto> findById(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        PermissionDto dto = permissionService.findById(id)
                .orElseThrow(() -> new ServiceException("Permission با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        permissionService.findById(id)
                .orElseThrow(() -> new ServiceException("Permission با این id یافت نشد: " + id));
        permissionService.deleteById(id);
        return RESULT(true, locale);
    }
}
