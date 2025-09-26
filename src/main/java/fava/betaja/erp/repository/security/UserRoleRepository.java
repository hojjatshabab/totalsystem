package fava.betaja.erp.repository.security;

import fava.betaja.erp.entities.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserId(Long userId);

    List<UserRole> findByRoleId(Long roleId);

    List<UserRole> findByRoleIdAndUserId(Long roleId, Long userId);
}
