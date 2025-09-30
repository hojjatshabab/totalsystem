package fava.betaja.erp.service.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleService {

    private Long id;
    private String name;
    private List<RolePermissionService> rolePermissions;
}
