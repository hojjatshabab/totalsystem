package fava.betaja.erp.entities.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_unit")
public class OrganizationUnit  {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "name")
    @Comment("نام")
    private String name;

    @Column(name = "address")
    @Comment("آدرس")
    private String address;

    @Column(name = "tell_number")
    @Comment("شماره تماس")
    private String tellNumber;

    @Column(name = "code")
    @Comment("کد")
    private String code;

    @Column(name = "active")
    @Comment("فعال/ غیرفعال")
    private Boolean active;

    @Column(name = "code_path")
    @Comment("کد سازمانی")
    private String codePath;

    @JoinColumn(name = "common_base_data_org_type_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Comment("نوع یگان")
    private CommonBaseData commonBaseDataOrgType;

    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Comment("یگان پدر")
    private OrganizationUnit parent;

}
