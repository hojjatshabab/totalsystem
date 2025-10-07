package fava.betaja.erp.repository.baseinfo;

import fava.betaja.erp.entities.baseinfo.FlowRuleDomain;
import fava.betaja.erp.repository.baseinfo.extra.FlowRuleDomainExtraRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlowRuleDomainRepository extends JpaRepository<FlowRuleDomain, UUID> , FlowRuleDomainExtraRepository {
    List<FlowRuleDomain> findByFlowRuleId(UUID flowRuleId);
    List<FlowRuleDomain> findByEntityNameIgnoreCase(String entityName);
}
