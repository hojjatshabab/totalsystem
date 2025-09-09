package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.AttributeValueDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeValueService {

    AttributeValueDto save(AttributeValueDto attributeValueDto);

    AttributeValueDto update(AttributeValueDto attributeValueDto);

    PageResponse<AttributeValueDto> findAll(PageRequest<AttributeValueDto> model);

    List<AttributeValueDto> findAll();

    Optional<AttributeValueDto> findById(UUID id);

    Boolean deleteById(UUID id);

}
