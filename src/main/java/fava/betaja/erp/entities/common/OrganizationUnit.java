package fava.betaja.erp.entities.common;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "organization_unit")
public class OrganizationUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "common_base_data_org_type_id")
    private Long orgTypeId;

    @ManyToOne
    @JoinColumn(name = "parent_id",referencedColumnName = "id",updatable = false,insertable = false)
    private OrganizationUnit organizationUnit;
}
