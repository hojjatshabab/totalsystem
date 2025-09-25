package fava.betaja.erp.controller.baseinfo;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleStepDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.baseinfo.FlowRuleStepService;
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
@RequestMapping("api/flow-rule-step")
@Tag(name = "مراحل جریان", description = "مدیریت مراحل قوانین جریان (FlowRuleStep)")
public class FlowRuleStepController extends BaseController {

    private final FlowRuleStepService flowRuleStepService;

    @PostMapping
    public ActionResult<FlowRuleStepDto> save(@RequestBody @Valid FlowRuleStepDto dto, Locale locale) {
        return RESULT(flowRuleStepService.save(dto), locale);
    }

    @PutMapping
    public ActionResult<FlowRuleStepDto> update(@RequestBody @Valid FlowRuleStepDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        flowRuleStepService.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("FlowRuleStep با این id یافت نشد: " + dto.getId()));
        return RESULT(flowRuleStepService.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<FlowRuleStepDto>> findAll(@RequestParam int currentPage,
                                                               @RequestParam int pageSize,
                                                               Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<FlowRuleStepDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<FlowRuleStepDto> response = flowRuleStepService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<FlowRuleStepDto>> list(Locale locale) {
        return RESULT(flowRuleStepService.findAll(), locale);
    }

    @GetMapping("/flow-rule/{flowRuleId}")
    public ActionResult<List<FlowRuleStepDto>> findByFlowRuleIdOrderByStepOrder(@PathVariable UUID flowRuleId,
                                                                                Locale locale) {
        if (flowRuleId == null) {
            return NO_CONTENT("flowRuleId", locale);
        }
        return RESULT(flowRuleStepService.findByFlowRuleIdOrderByStepOrder(flowRuleId), locale);
    }

    @GetMapping("/previous-step/{previousStepId}")
    public ActionResult<List<FlowRuleStepDto>> findByPreviousStepId(@PathVariable UUID previousStepId,
                                                                    Locale locale) {
        if (previousStepId == null) {
            return NO_CONTENT("previousStepId", locale);
        }
        return RESULT(flowRuleStepService.findByPreviousStepId(previousStepId), locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<FlowRuleStepDto> findById(@PathVariable UUID id, Locale locale) {
        Optional<FlowRuleStepDto> dto = flowRuleStepService.findById(id);
        return RESULT(dto.orElseThrow(() -> new ServiceException("FlowRuleStep با این id یافت نشد: " + id)), locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        flowRuleStepService.findById(id)
                .orElseThrow(() -> new ServiceException("FlowRuleStep با این id یافت نشد: " + id));
        flowRuleStepService.deleteById(id);
        return RESULT(true, locale);
    }
}

