package fava.betaja.erp.dto.baseinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowRuleDto {

    private UUID id;
    private String name;
    private String description;
    private Boolean active = true;
    private List<FlowRuleStepDto> steps;
    private List<FlowRuleDomainDto> domains;

}
