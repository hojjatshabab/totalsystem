package fava.betaja.erp.repository.common.extra.Impl;

import fava.betaja.erp.entities.common.CommonBaseData;
import fava.betaja.erp.repository.common.extra.CommonBaseDataExtraRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommonBaseDataExtraRepositoryImpl implements CommonBaseDataExtraRepository {

    private final EntityManager entityManager;

    @Override
    public List<CommonBaseData> findByCommonBaseTypeIdAndValueContainingIgnoreCase(Long commonBaseType, String values) {

        String querySt = " select c from CommonBaseData c where 1=1 and c.active=true ";

        if (commonBaseType != null) {
            querySt += " and c.commonBaseType.id =:commonBaseType ";
        }

        if (values != null) {
            querySt += " and c.value like :val ";
        }
        querySt += " order by c.orderNo asc limit 20 ";

        jakarta.persistence.Query query = entityManager.createQuery(querySt);
        if (commonBaseType != null) {
            query.setParameter("commonBaseType", commonBaseType);
        }
        if (values != null) {
            query.setParameter("val", "%" + values + "%");
        }

        List<CommonBaseData> dataList = (List<CommonBaseData>) query.getResultList();
        return dataList.size() > 0 ? dataList : new ArrayList<>();

    }
}
