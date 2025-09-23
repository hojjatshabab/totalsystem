package fava.betaja.erp.repository.common.extra;

import fava.betaja.erp.entities.common.CommonBaseData;

import java.util.List;

public interface CommonBaseDataExtraRepository {
    List<CommonBaseData> findByCommonBaseTypeIdAndValueContainingIgnoreCase(Long commonBaseTypeId, String values);
}
