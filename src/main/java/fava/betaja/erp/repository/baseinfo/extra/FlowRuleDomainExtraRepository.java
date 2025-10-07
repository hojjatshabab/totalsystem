package fava.betaja.erp.repository.baseinfo.extra;

import fava.betaja.erp.entities.baseinfo.FlowRuleDomain;

public interface FlowRuleDomainExtraRepository {
    FlowRuleDomain findFirstCandidate(String entityName, String flowCode);
}
