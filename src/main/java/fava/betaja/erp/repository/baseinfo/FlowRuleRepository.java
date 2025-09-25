package fava.betaja.erp.repository.baseinfo;

import fava.betaja.erp.entities.baseinfo.FlowRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlowRuleRepository extends JpaRepository<FlowRule, UUID> {
    FlowRule findByName(String name);
    List<FlowRule> findByActiveTrue();
}
