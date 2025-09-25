package fava.betaja.erp.entities.baseinfo;

import fava.betaja.erp.entities.AbstractAuditingEntity;
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
@Table(name = "flow_rule", schema = "base_info")
public class FlowRule extends AbstractAuditingEntity {

    @Column(name = "name", nullable = false, length = 255, unique = true)
    @Comment("نام جریان")
    private String name;

    @Column(name = "description", length = 1000)
    @Comment("توضیحات تکمیلی جریان")
    private String description;

    @Column(name = "active", nullable = false)
    @Comment("فعال/غیرفعال")
    private Boolean active = true;

    @OneToMany(mappedBy = "flowRule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Comment("مراحل جریان")
    private List<FlowRuleStep> steps;

    @OneToMany(mappedBy = "flowRule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Comment("دامنه‌های جریان")
    private List<FlowRuleDomain> domains;

}
