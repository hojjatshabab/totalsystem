package fava.betaja.erp.repository.common;

import fava.betaja.erp.entities.common.CommonBaseData;
import fava.betaja.erp.repository.common.extra.CommonBaseDataExtraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonBaseDataRepository extends JpaRepository<CommonBaseData, Long> , CommonBaseDataExtraRepository {
    Page<CommonBaseData> findByCommonBaseTypeIdAndActiveTrue(Long commonBaseTypeId, Pageable pageable);

    List<CommonBaseData> findByCommonBaseTypeIdOrderByOrderNoAsc(Long commonBaseTypeId);

    List<CommonBaseData> findByCommonBaseTypeIdAndActiveTrue(Long commonBaseTypeId);

    Page<CommonBaseData> findByValueContainingIgnoreCaseAndCommonBaseTypeIdAndActiveTrue(String value, Long commonBaseTypeId, Pageable pageable);
}
