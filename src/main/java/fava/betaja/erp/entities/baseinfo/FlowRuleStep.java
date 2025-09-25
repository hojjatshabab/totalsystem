package fava.betaja.erp.entities.baseinfo;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.Role;
import fava.betaja.erp.enums.baseinfo.ActionTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flow_rule_step", schema = "base_info",
        uniqueConstraints = @UniqueConstraint(columnNames = {"flow_rule_id", "step_order"})
        , indexes = {
        @Index(columnList = "organization_unit_id"),
        @Index(columnList = "role_id")}
)
public class FlowRuleStep extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flow_rule_id", referencedColumnName = "id", nullable = false)
    @Comment("جریان اصلی")
    private FlowRule flowRule;

    @Column(name = "step_order", nullable = false)
    @Comment("ترتیب مرحله در جریان")
    private Integer stepOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_unit_id")
    @Comment("یگان مسئول مرحله")
    private OrganizationUnit organizationUnit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    @Comment("نقش مسئول مرحله")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", length = 50, nullable = false)
    @Comment("نوع اقدام")
    private ActionTypeEnum actionType = ActionTypeEnum.APPROVE;

    @Column(name = "condition_expression", length = 1000)
    @Comment("شرط حرکت به مرحله بعدی")
    private String conditionExpression;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_step_id")
    @Comment("مرحله قبلی")
    private FlowRuleStep previousStep;

    @OneToMany(mappedBy = "previousStep", fetch = FetchType.LAZY)
    @Comment("مراحل بعدی در صورت branching یا شرط")
    private List<FlowRuleStep> nextSteps;

    @Column(name = "is_final", nullable = false)
    @Comment("آیا این مرحله پایان جریان است؟")
    private Boolean finalStep = false;

}
