package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.AttributeValueDto;
import fava.betaja.erp.entities.da.AttributeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeValueService {

    AttributeValueDto save(AttributeValueDto attributeValueDto);

    AttributeValueDto update(AttributeValueDto attributeValueDto);

    PageResponse<AttributeValueDto> findAll(PageRequest<AttributeValueDto> model);

    PageResponse<AttributeValueDto> findByAttributePeriodId(UUID attributePeriodId,PageRequest<AttributeValueDto> model);

    List<AttributeValueDto> findAll();

    Optional<AttributeValueDto> findById(UUID id);

    void deleteById(UUID id);

}
