package fava.betaja.erp.entities.baseinfo;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flow_rule_domain", schema = "base_info",
        uniqueConstraints = @UniqueConstraint(columnNames = {"flow_rule_id", "entity_name"}))
public class FlowRuleDomain extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flow_rule_id", referencedColumnName = "id", nullable = false)
    @Comment("جریان مربوطه")
    private FlowRule flowRule;

    @Column(name = "entity_name", nullable = false, length = 255)
    @Comment("نام Entity هدف")
    private String entityName;

    @Column(name = "active", nullable = false)
    @Comment("فعال/غیرفعال")
    private Boolean active = true;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "flow_code")
    private String flowCode;


}
