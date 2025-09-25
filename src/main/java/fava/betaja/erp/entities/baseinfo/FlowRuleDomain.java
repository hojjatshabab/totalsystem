package fava.betaja.erp.entities.baseinfo;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import fava.betaja.erp.enums.baseinfo.FlowDomainEnum;
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
        uniqueConstraints = @UniqueConstraint(columnNames = {"flow_rule_id", "domain"}))
public class FlowRuleDomain extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flow_rule_id", referencedColumnName = "id", nullable = false)
    @Comment("جریان مربوطه")
    private FlowRule flowRule;

    @Enumerated(EnumType.STRING)
    @Column(name = "domain", length = 50)
    @Comment("دامنه جریان")
    private FlowDomainEnum domain;

    @Column(name = "entity_name", nullable = false, length = 255)
    @Comment("نام Entity هدف")
    private String entityName;

    @Column(name = "active", nullable = false)
    @Comment("فعال/غیرفعال")
    private Boolean active = true;

}
