package fava.betaja.erp.repository.baseinfo;

import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlowRuleStepRepository extends JpaRepository<FlowRuleStep, UUID> {
    List<FlowRuleStep> findByFlowRuleIdOrderByStepOrder(UUID flowRuleId);
    List<FlowRuleStep> findByPreviousStepId(UUID previousStepId);
}
