package fava.betaja.erp.service.baseinfo;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleDomainDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlowRuleDomainService {

    FlowRuleDomainDto save(FlowRuleDomainDto flowRuleDomainDto);

    FlowRuleDomainDto update(FlowRuleDomainDto flowRuleDomainDto);

    PageResponse<FlowRuleDomainDto> findAll(PageRequest<FlowRuleDomainDto> model);

    List<FlowRuleDomainDto> findAll();

    List<FlowRuleDomainDto> findByFlowRuleId(UUID flowRuleId);

    List<FlowRuleDomainDto> findByEntityName(String entityName);

    Optional<FlowRuleDomainDto> findById(UUID id);

    void deleteById(UUID id);


}

