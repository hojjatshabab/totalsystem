package fava.betaja.erp.service.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionService {

    private Long id;
    private Long roleId;
    private PermissionService permission;
}
