package fava.betaja.erp.controller.common;


import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.common.CommonTypeDto;
import fava.betaja.erp.service.common.CommonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("api/v1/common-type")
@RequiredArgsConstructor
public class CommonTypeController extends BaseController {

    public final CommonTypeService service;

    @GetMapping("/find-all")
    public ActionResult< Page<CommonTypeDto> > findAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size, Locale locale) {
        if (page <= 0 || size <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        Pageable pageable = PageRequest.of(page, size);
        return RESULT(service.findAll(pageable),locale);
    }

    @GetMapping("/find-by-name")
    public ActionResult<Page<CommonTypeDto>> findByName(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,@RequestParam String name,Locale locale) {
        if (page <= 0 || size <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        Pageable pageable = PageRequest.of(page, size);
        return RESULT(service.findByName(pageable,name),locale);
    }

}
