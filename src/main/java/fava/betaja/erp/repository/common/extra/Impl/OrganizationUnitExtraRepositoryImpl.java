package fava.betaja.erp.repository.common.extra.Impl;

import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.repository.common.extra.OrganizationUnitExtraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class OrganizationUnitExtraRepositoryImpl implements OrganizationUnitExtraRepository {

    private final EntityManager entityManager;

    @Override
    public String findUniqueCodePathByParentCodePath(String parentCodePath) {
        if (parentCodePath.isEmpty()) return null;
        String querySt = " select o from OrganizationUnit o where o.codePath like :value order by o.codePath desc ";
        Query query = entityManager.createQuery(querySt);
        query.setParameter("value", parentCodePath + "%");
        query.setMaxResults(1);
        List<OrganizationUnit> units = query.getResultList();
        String tempCodePath = units.get(0).getCodePath();
        if (tempCodePath.trim().length() == parentCodePath.trim().length()) return tempCodePath.concat("001");
        else {
            Integer intCodePath = Integer.valueOf(tempCodePath.trim()) + 1;
            return "00".concat(intCodePath.toString());
        }
    }

    @Override
    public Optional<List<OrganizationUnit>> findByParentIdOrderByCodePathAsc(Long parentId) {
        if (Objects.isNull(parentId)) return Optional.ofNullable(new ArrayList<>());
        String querySt = " select o from OrganizationUnit o where o.parent.id =:parentId order by o.codePath asc ";
        Query query = entityManager.createQuery(querySt);
        query.setParameter("parentId", parentId);
        return Optional.ofNullable((List<OrganizationUnit>) query.getResultList());
    }

    @Override
    public Optional<OrganizationUnit> findByUnitCode(String code) {
        String querySt = " select o from OrganizationUnit o where o.code =:code";
        Query query = entityManager.createQuery(querySt);
        query.setParameter("code", code);
        return Optional.ofNullable((OrganizationUnit) query.getSingleResult());
    }

}
