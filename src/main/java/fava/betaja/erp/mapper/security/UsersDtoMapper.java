package fava.betaja.erp.mapper.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDtoMapper {

    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private Boolean active = true;
    private List<UserRoleDtoMapper> userRoles;
    private Long organizationUnitId;
    private String organizationUnitName;
}
