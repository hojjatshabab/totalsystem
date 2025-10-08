package fava.betaja.erp.repository.security.extra;

import fava.betaja.erp.entities.security.Users;

public interface UserExtraRepository {

    Users findFirstByRoleIdAndOrganizationUnitId(Long roleId, Long orgUnitId);
}
