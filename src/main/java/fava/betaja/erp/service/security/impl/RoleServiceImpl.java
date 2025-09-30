package fava.betaja.erp.service.security.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleServiceImpl {

    private Long id;
    private String name;
    private List<RolePermissionServiceImpl> rolePermissions;
}
