package fava.betaja.erp.entities.security;


import fava.betaja.erp.entities.security.Permission;
import fava.betaja.erp.entities.security.Role;
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
@Table(name = "role_permission")
public class RolePermission {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "role_id")
    private Long RoleId;

    @Column(name = "permission_id")
    private Long PermissionId;
    @ManyToOne
    @JoinColumn(name = "role_id" ,referencedColumnName = "id",updatable = false,insertable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id" ,referencedColumnName = "id",updatable = false,insertable = false)  // This is the foreign key column in Order table
    private Permission permission;

}
