package fava.betaja.erp.service.security.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleServiceImpl {

    private Long id;
    private UsersServiceImpl userId;
    private String userName;
    private Long roleId;
    private String roleName;
}
