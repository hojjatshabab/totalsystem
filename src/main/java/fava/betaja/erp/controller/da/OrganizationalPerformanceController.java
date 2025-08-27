package fava.betaja.erp.controller.da;


import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.da.OrganizationalPerformanceDto;
import fava.betaja.erp.entities.da.OrganizationalPerformance;
import fava.betaja.erp.repository.da.OrganizationalPerformanceRepository;
import fava.betaja.erp.service.da.OrganizationalPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/org-performance")
@RequiredArgsConstructor
public class OrganizationalPerformanceController extends BaseController {

    @Autowired
    private final OrganizationalPerformanceService performanceService;

    @GetMapping("/find-all")
    public Page<OrganizationalPerformanceDto> findAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return performanceService.findAll(pageable);
    }

    @PostMapping("/save")
    public ActionResult<OrganizationalPerformanceDto> save(@RequestBody OrganizationalPerformanceDto dto, Locale locale) {
        try {
            return RESULT(performanceService.save(dto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }

    }

    @PutMapping("/update")
    public ActionResult<OrganizationalPerformanceDto> update(@RequestBody OrganizationalPerformanceDto dto, Locale locale) {
        try {
            return RESULT(performanceService.save(dto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }






    @DeleteMapping("/remove/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!performanceService.findById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        try {
            return RESULT(performanceService.deleteById(id), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

}
