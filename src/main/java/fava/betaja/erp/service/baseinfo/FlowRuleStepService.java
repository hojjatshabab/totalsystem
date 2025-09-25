package fava.betaja.erp.service.baseinfo;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleStepDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlowRuleStepService {

    FlowRuleStepDto save(FlowRuleStepDto flowRuleStepDto);

    FlowRuleStepDto update(FlowRuleStepDto flowRuleStepDto);

    PageResponse<FlowRuleStepDto> findAll(PageRequest<FlowRuleStepDto> model);

    List<FlowRuleStepDto> findAll();

    List<FlowRuleStepDto> findByFlowRuleIdOrderByStepOrder(UUID flowRuleId);

    List<FlowRuleStepDto> findByPreviousStepId(UUID previousStepId);

    Optional<FlowRuleStepDto> findById(UUID id);

    void deleteById(UUID id);

}

