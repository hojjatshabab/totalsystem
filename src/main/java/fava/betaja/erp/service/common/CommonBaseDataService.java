package fava.betaja.erp.service.common;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.CommonBaseDataDto;
import fava.betaja.erp.dto.common.CommonBaseTypeDto;
import fava.betaja.erp.entities.common.CommonBaseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommonBaseDataService {
    CommonBaseDataDto save(CommonBaseDataDto dto);

    CommonBaseDataDto update(CommonBaseDataDto dto);

    PageResponse<CommonBaseDataDto> findAll(PageRequest<CommonBaseDataDto> model);

    PageResponse<CommonBaseData> findByCommonBaseTypeIdAndActiveTrue(Long commonBaseTypeId, PageRequest<CommonBaseDataDto> model);

    List<CommonBaseData> findByCommonBaseTypeIdOrderByOrderNoAsc(Long commonBaseTypeId);

    List<CommonBaseData> findByCommonBaseTypeIdAndActiveTrue(Long commonBaseTypeId);

    PageResponse<CommonBaseData> findByValueContainsAndCommonBaseTypeIdAndActiveTrue(String value, Long commonBaseTypeId, PageRequest<CommonBaseDataDto> model);

    List<CommonBaseDataDto> findByCommonBaseTypeAndValueContain(Long commonBaseTypeId, String values);

    List<CommonBaseDataDto> findAll();

    Optional<CommonBaseDataDto> findById(Long id);

    void deleteById(Long id);

}
