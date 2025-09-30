package fava.betaja.erp.service.security;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.RolePermissionDto;
import fava.betaja.erp.entities.security.RolePermission;

import java.util.List;
import java.util.Optional;

public interface RolePermissionService {

    RolePermissionDto save(RolePermissionDto rolePermissionDto);

    RolePermissionDto update(RolePermissionDto rolePermissionDto);

    PageResponse<RolePermissionDto> findAll(PageRequest<RolePermissionDto> model);

    List<RolePermissionDto> findAll();

    List<RolePermissionDto> findByPermissionId(Long permissionId);

    List<RolePermissionDto> findByRoleId(Long roleId);

    List<RolePermissionDto> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    Optional<RolePermissionDto> findById(Long id);

    void deleteById(Long id);

}
