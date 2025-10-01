package fava.betaja.erp.controller.security;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.UsersDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.security.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Tag(name = "کاربران", description = "مدیریت کابران")
public class UsersController extends BaseController {

    private final UsersService usersService;

    @PostMapping
    public ActionResult<UsersDto> save(@RequestBody @Valid UsersDto dto, Locale locale) {
        return RESULT(usersService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<UsersDto> update(@RequestBody @Valid UsersDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        usersService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("Users با این id یافت نشد: " + dto.getId()));
        return RESULT(usersService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<UsersDto>> findAll(@RequestParam int currentPage,
                                                        @RequestParam int pageSize,
                                                        Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<UsersDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<UsersDto> response = usersService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<UsersDto>> list(Locale locale) {
        List<UsersDto> dtoList = usersService.findAll();
        return RESULT(dtoList, locale);
    }

    @GetMapping("/username/{username}")
    public ActionResult<UsersDto> findByName(@PathVariable String username, Locale locale) {
        if (username == null || username.isBlank()) {
            return NO_CONTENT("id", locale);
        }
        UsersDto dto = usersService.findByUsername(username)
                .orElseThrow(() -> new ServiceException("Users با این id یافت نشد: " + username));
        return RESULT(dto, locale);
    }

    @GetMapping("/current-org")
    public ActionResult<OrganizationUnit> getCurrentUserOrganizationUnit(Locale locale) {
        OrganizationUnit entity = usersService.getCurrentUserOrganizationUnit();
        return RESULT(entity, locale);
    }

    @GetMapping("/current-user")
    public ActionResult<Users> getCurrentUser(Locale locale) {
        Users entity = usersService.getCurrentUser();
        return RESULT(entity, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<UsersDto> findById(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        UsersDto dto = usersService.findById(id)
                .orElseThrow(() -> new ServiceException("Users= با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        usersService.findById(id)
                .orElseThrow(() -> new ServiceException("Users با این id یافت نشد: " + id));
        usersService.deleteById(id);
        return RESULT(true, locale);
    }
}
