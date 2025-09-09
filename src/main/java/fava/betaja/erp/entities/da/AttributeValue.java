package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.BaseEntity;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.enums.ValueType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attribute_value", schema = "da")
public class AttributeValue extends BaseEntity {

    @Column(name = "value")
    @Comment("مقدار")
    private Integer value;

    @Column(name = "value_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("نوع مقدار")
    private ValueType valueType;

    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Comment("ویژگی")
    private Attribute attribute;

    @JoinColumn(name = "data_period_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Comment("دیتای دوره زمانی")
    private DataPeriod dataPeriod;

}
