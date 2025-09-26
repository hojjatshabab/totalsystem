package fava.betaja.erp.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    private Long id;
    private UsersDto userId;
    private String userName;
    private Long roleId;
    private String roleName;
}
