package fava.betaja.erp.service.common;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.CommonBaseTypeDto;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.entities.common.CommonBaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CommonBaseTypeService {
    CommonBaseTypeDto save(CommonBaseTypeDto dto);

    CommonBaseTypeDto update(CommonBaseTypeDto dto);

    PageResponse<CommonBaseTypeDto> findAll(PageRequest<CommonBaseTypeDto> model);

    PageResponse<CommonBaseTypeDto> findByClassNameContainingOrTitleContaining(String className, String title, PageRequest<CommonBaseTypeDto> model);

    PageResponse<CommonBaseTypeDto> findByClassNameContainingAndTitleContaining(String className, String title, PageRequest<CommonBaseTypeDto> model);

    List<CommonBaseTypeDto> findByTitleContains(String Title);

    List<CommonBaseTypeDto> findAll();

    Optional<CommonBaseTypeDto> findById(Long id);

    Optional<CommonBaseTypeDto> findByClassName(String className);

    void deleteById(Long id);

}
