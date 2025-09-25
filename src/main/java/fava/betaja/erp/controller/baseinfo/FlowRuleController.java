package fava.betaja.erp.controller.baseinfo;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.baseinfo.FlowRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/flow-rule")
@Tag(name = "جریان‌ها", description = "مدیریت قوانین جریان (FlowRule)")
public class FlowRuleController extends BaseController {

    private final FlowRuleService flowRuleService;

    @PostMapping
    public ActionResult<FlowRuleDto> save(@RequestBody @Valid FlowRuleDto dto, Locale locale) {
        return RESULT(flowRuleService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<FlowRuleDto> update(@RequestBody @Valid FlowRuleDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        flowRuleService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("FlowRule با این id یافت نشد: " + dto.getId()));
        return RESULT(flowRuleService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<FlowRuleDto>> findAll(@RequestParam int currentPage,
                                                           @RequestParam int pageSize,
                                                           Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<FlowRuleDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<FlowRuleDto> response = flowRuleService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<FlowRuleDto>> list(Locale locale) {
        return RESULT(flowRuleService.findAll(), locale);
    }

    @GetMapping("/active")
    public ActionResult<List<FlowRuleDto>> findByActiveTrue(Locale locale) {
        return RESULT(flowRuleService.findByActiveTrue(), locale);
    }

    @GetMapping("/name/{name}")
    public ActionResult<FlowRuleDto> findByName(@PathVariable String name, Locale locale) {
        if (name == null || name.isBlank()) {
            return NO_CONTENT("name", locale);
        }
        FlowRuleDto dto = flowRuleService.findByName(name);
        if (dto == null) {
            throw new ServiceException("FlowRule با این نام یافت نشد: " + name);
        }
        return RESULT(dto, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<FlowRuleDto> findById(@PathVariable UUID id, Locale locale) {
        Optional<FlowRuleDto> dto = flowRuleService.findById(id);
        return RESULT(dto.orElseThrow(() -> new ServiceException("FlowRule با این id یافت نشد: " + id)), locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        flowRuleService.findById(id)
                .orElseThrow(() -> new ServiceException("FlowRule با این id یافت نشد: " + id));
        flowRuleService.deleteById(id);
        return RESULT(true, locale);
    }
}

