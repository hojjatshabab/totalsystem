package fava.betaja.erp.repository.da.extra.impl;

import fava.betaja.erp.entities.da.Project;
import fava.betaja.erp.repository.da.extra.ProjectExtraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectExtraRepositoryImpl implements ProjectExtraRepository {

    private final EntityManager entityManager;

    @Override
    public List<Project> findByOrganizationUnitId(Long organizationId) {
        String querySt = " SELECT p FROM Project p " +
                " WHERE p.plan.organizationUnit.id = :organizationId ";

        Query query = entityManager.createQuery(querySt);

        query.setParameter("organizationId", organizationId);

        return query.getResultList();
    }
}
