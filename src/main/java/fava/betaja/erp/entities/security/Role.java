package fava.betaja.erp.entities.security;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "role_name")
    private String name;

    @OneToMany(mappedBy = "role")
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "role",fetch = FetchType.EAGER    )
    private List<RolePermission> rolePermissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
