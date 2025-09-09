package fava.betaja.erp.controller.common;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.dto.da.OrganizationalPerformanceDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.service.common.OrganizationUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("api/organization")
@RequiredArgsConstructor
public class OrganizationUnitController extends BaseController {


    private final OrganizationUnitService service;

    @PostMapping
    public ActionResult<OrganizationUnitDto> save(@RequestBody OrganizationUnitDto dto, Locale locale) {
        try {
            return RESULT(service.save(dto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping
    public Page<OrganizationUnitDto> findAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.findAll(pageable);
    }

    @DeleteMapping("/delete/{id}")
    public ActionResult<Boolean> delete(@PathVariable Long id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!service.findById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        try {
            return RESULT(service.deleteById(id), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }

    }

    @PutMapping
    public ActionResult<OrganizationUnitDto> update(@RequestBody OrganizationUnitDto dto, Locale locale) {
        try {
            return RESULT(service.save(dto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }
}
