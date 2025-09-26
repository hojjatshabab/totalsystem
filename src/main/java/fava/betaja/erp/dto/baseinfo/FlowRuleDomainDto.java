package fava.betaja.erp.dto.baseinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowRuleDomainDto {

    private UUID id;
    private UUID flowRuleId;
    private String flowRuleName;
    private String entityName;
    private Boolean active = true;
}
