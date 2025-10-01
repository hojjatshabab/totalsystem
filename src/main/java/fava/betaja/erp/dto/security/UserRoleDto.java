package fava.betaja.erp.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {

    private Long id;
    private Long userId;
    private String username;
    private String firstname;
    private String lastname;
    private Long roleId;
    private String roleName;
    private String roleTitle;
}
