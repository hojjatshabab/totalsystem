package fava.betaja.erp.entities.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class Users implements UserDetails { // make our app User a spring security User
    /*
        we have two options : implements the UserDetails interface or create a user class that extends User spring class which also
        implements UserDetails
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    private String username;

    private String password;

    private Boolean active;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> userRoles;

    // we should return a list of roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userRoles == null) return null;
        List<String> authorities = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            authorities
                    .addAll(userRole.getRole().getRolePermissions().stream()
                            .map(a -> a.getPermission().getAuthority()).toList());

        }
        return authorities.stream().map(a -> new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return a;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
