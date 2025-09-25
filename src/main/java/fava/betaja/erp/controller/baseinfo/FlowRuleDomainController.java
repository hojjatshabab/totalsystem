package fava.betaja.erp.controller.baseinfo;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleDomainDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.baseinfo.FlowRuleDomainService;
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
@RequestMapping("api/flow-rule-domain")
@Tag(name = "دامنه‌های جریان", description = "مدیریت دامنه‌های مرتبط با FlowRule")
public class FlowRuleDomainController extends BaseController {

    private final FlowRuleDomainService flowRuleDomainService;

    @PostMapping
    public ActionResult<FlowRuleDomainDto> save(@RequestBody @Valid FlowRuleDomainDto dto, Locale locale) {
        return RESULT(flowRuleDomainService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<FlowRuleDomainDto> update(@RequestBody @Valid FlowRuleDomainDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        flowRuleDomainService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("FlowRuleDomain با این id یافت نشد: " + dto.getId()));
        return RESULT(flowRuleDomainService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<FlowRuleDomainDto>> findAll(@RequestParam int currentPage,
                                                                 @RequestParam int pageSize,
                                                                 Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<FlowRuleDomainDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<FlowRuleDomainDto> response = flowRuleDomainService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<FlowRuleDomainDto>> list(Locale locale) {
        return RESULT(flowRuleDomainService.findAll(), locale);
    }

    @GetMapping("/flow-rule/{flowRuleId}")
    public ActionResult<List<FlowRuleDomainDto>> findByFlowRuleId(@PathVariable UUID flowRuleId, Locale locale) {
        if (flowRuleId == null) {
            return NO_CONTENT("flowRuleId", locale);
        }
        return RESULT(flowRuleDomainService.findByFlowRuleId(flowRuleId), locale);
    }

    @GetMapping("/entity/{entityName}")
    public ActionResult<List<FlowRuleDomainDto>> findByEntityName(@PathVariable String entityName, Locale locale) {
        if (entityName == null || entityName.isBlank()) {
            return NO_CONTENT("entityName", locale);
        }
        return RESULT(flowRuleDomainService.findByEntityName(entityName), locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<FlowRuleDomainDto> findById(@PathVariable UUID id, Locale locale) {
        Optional<FlowRuleDomainDto> dto = flowRuleDomainService.findById(id);
        return RESULT(dto.orElseThrow(() -> new ServiceException("FlowRuleDomain با این id یافت نشد: " + id)), locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        flowRuleDomainService.findById(id)
                .orElseThrow(() -> new ServiceException("FlowRuleDomain با این id یافت نشد: " + id));
        flowRuleDomainService.deleteById(id);
        return RESULT(true, locale);
    }
}

