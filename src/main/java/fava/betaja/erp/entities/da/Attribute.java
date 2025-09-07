package fava.betaja.erp.entities.da;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.BaseEntity;
import fava.betaja.erp.entities.common.OrganizationUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attribute", schema = "da")
public class Attribute extends BaseEntity {

    @Column(name = "name")
    @Comment("نام")
    private String name;

    @Column(name = "code")
    @Comment("کد")
    private String code;

    @Column(name = "data_type")
    @Comment("نوع داده")
    private String dataType;

    @JoinColumn(name = "organization_unit_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Comment("یگان")
    private OrganizationUnit organizationUnit;

}
