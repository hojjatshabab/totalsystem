package fava.betaja.erp.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionDto {

    private Long id;
    private Long roleId;
    private PermissionDto permission;
}
