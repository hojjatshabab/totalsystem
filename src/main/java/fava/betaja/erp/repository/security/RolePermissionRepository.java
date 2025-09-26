package fava.betaja.erp.repository.security;

import fava.betaja.erp.entities.security.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByPermissionId(Long permissionId);

    List<RolePermission> findByRoleId(Long roleId);

    List<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
