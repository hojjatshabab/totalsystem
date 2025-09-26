package fava.betaja.erp.repository.security;

import fava.betaja.erp.entities.security.Role;
import fava.betaja.erp.entities.security.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
