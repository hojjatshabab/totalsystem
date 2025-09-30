package fava.betaja.erp.controller.security;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.UserRoleDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.security.UserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user-role")
@Tag(name = "نقش های کاربران", description = "مدیریت نقش هر کاربر")
public class UserRoleController extends BaseController {

    private final UserRoleService service;

    @PostMapping
    public ActionResult<UserRoleDto> save(@RequestBody @Valid UserRoleDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<UserRoleDto> update(@RequestBody @Valid UserRoleDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("UserRole با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<UserRoleDto>> findAll(@RequestParam int currentPage,
                                                           @RequestParam int pageSize,
                                                           Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<UserRoleDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<UserRoleDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<UserRoleDto>> list(Locale locale) {
        List<UserRoleDto> dtoList = service.findAll();
        return RESULT(dtoList, locale);
    }

    @GetMapping("/find-by-role-user")
    public ActionResult<List<UserRoleDto>> findByRoleIdAndUserId(@RequestParam Long roleId, @RequestParam Long userId, Locale locale) {
        List<UserRoleDto> dtoList = service.findByRoleIdAndUserId(roleId, userId);
        return RESULT(dtoList, locale);
    }

    @GetMapping("/find-by-user")
    public ActionResult<List<UserRoleDto>> findByUserId(@RequestParam Long userId, Locale locale) {
        List<UserRoleDto> dtoList = service.findByUserId(userId);
        return RESULT(dtoList, locale);
    }

    @GetMapping("/find-by-role")
    public ActionResult<List<UserRoleDto>> findByRoleId(@RequestParam Long roleId, Locale locale) {
        List<UserRoleDto> dtoList = service.findByRoleId(roleId);
        return RESULT(dtoList, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<UserRoleDto> findById(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        UserRoleDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("UserRole= با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("UserRole با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
