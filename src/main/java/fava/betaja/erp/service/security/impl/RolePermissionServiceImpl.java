package fava.betaja.erp.service.security.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionServiceImpl {

    private Long id;
    private Long roleId;
    private PermissionServiceImpl permission;
}
