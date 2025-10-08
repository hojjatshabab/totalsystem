package fava.betaja.erp.repository.security.extra.impl;

import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.repository.security.extra.UserExtraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserExtraRepositoryImpl implements UserExtraRepository {

    private final EntityManager entityManager;

    @Override
    public Users findFirstByRoleIdAndOrganizationUnitId(Long roleId, Long orgUnitId) {
        String querySt = "SELECT u FROM Users u " +
                " JOIN u.userRoles ur " +
                " WHERE ur.role.id = :roleId " +
                " AND u.organizationUnit.id = :orgUnitId " +
                " AND u.active = true " +
                " ORDER BY u.id ASC ";

        Query query = entityManager.createQuery(querySt);
        query.setParameter("roleId", roleId);
        query.setParameter("orgUnitId", orgUnitId);
        query.setMaxResults(1);

        List<Users> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
