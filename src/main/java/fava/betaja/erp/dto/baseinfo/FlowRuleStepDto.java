package fava.betaja.erp.dto.baseinfo;

import fava.betaja.erp.enums.baseinfo.ActionTypeEnum;
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
public class FlowRuleStepDto {

    private UUID id;
    private UUID flowRuleId;
    private String flowRuleName;
    private Integer stepOrder;
    private Long organizationUnitId;
    private String organizationUnitName;
    private Long roleId;
    private String roleName;
    private ActionTypeEnum actionType = ActionTypeEnum.APPROVE;
    private String conditionExpression;
    private FlowRuleStepDto previousStep;
    private List<FlowRuleStepDto> nextSteps;
    private Boolean finalStep = false;

}
