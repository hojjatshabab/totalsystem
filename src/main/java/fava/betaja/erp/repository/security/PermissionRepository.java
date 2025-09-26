package fava.betaja.erp.repository.security;

import fava.betaja.erp.entities.security.Permission;
import fava.betaja.erp.entities.security.Role;
import fava.betaja.erp.entities.security.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}
