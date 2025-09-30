package fava.betaja.erp.service.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleService {

    private Long id;
    private UsersService userId;
    private String userName;
    private Long roleId;
    private String roleName;
}
