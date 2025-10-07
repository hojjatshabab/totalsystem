package fava.betaja.erp.repository.baseinfo.extra.impl;

import fava.betaja.erp.entities.baseinfo.FlowRuleDomain;
import fava.betaja.erp.repository.baseinfo.extra.FlowRuleDomainExtraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FlowRuleDomainExtraRepositoryImpl implements FlowRuleDomainExtraRepository {

    private final EntityManager entityManager;

    @Override
    public FlowRuleDomain findFirstCandidate(String entityName, String flowCode) {

        String querySt = " SELECT f FROM FlowRuleDomain f " +
                " WHERE f.entityName = :entityName " +
                " AND f.active = true ";

        if (flowCode != null && !flowCode.isBlank()) {
            querySt += " AND f.flowCode = :flowCode ";
        }

        querySt += " ORDER BY CASE WHEN f.isDefault = true THEN 0 ELSE 1 END ";

        Query query = entityManager.createQuery(querySt);

        query.setParameter("entityName", entityName);

        if (flowCode != null && !flowCode.isBlank()) {
            query.setParameter("flowCode", flowCode);
        }

        query.setMaxResults(1);

        List<FlowRuleDomain> result = query.getResultList();

        return result.isEmpty() ? null : result.get(0);
    }
}
