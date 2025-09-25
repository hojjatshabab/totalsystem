package fava.betaja.erp.dto.baseinfo;

import fava.betaja.erp.enums.baseinfo.FlowDomainEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowRuleDomainDto {

    private UUID id;
    private UUID flowRuleId;
    private String flowRuleName;
    private FlowDomainEnum domain;
    private String entityName;
    private Boolean active = true;
}
