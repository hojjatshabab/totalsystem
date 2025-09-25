package fava.betaja.erp.service.baseinfo;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlowRuleService {

    FlowRuleDto save(FlowRuleDto flowRuleDto);

    FlowRuleDto update(FlowRuleDto flowRuleDto);

    PageResponse<FlowRuleDto> findAll(PageRequest<FlowRuleDto> model);

    List<FlowRuleDto> findAll();

    FlowRuleDto findByName(String name);

    List<FlowRuleDto> findByActiveTrue();

    Optional<FlowRuleDto> findById(UUID id);

    void deleteById(UUID id);
}

