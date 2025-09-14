package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeService {

    AttributeDto save(AttributeDto attributeDto);

    AttributeDto update(AttributeDto attributeDto);

    PageResponse<AttributeDto> findAll(PageRequest<AttributeDto> model);

    PageResponse<AttributeDto> findByOrganizationUnitId(Long organizationUnitId, PageRequest<AttributeDto> model);

    List<AttributeDto> findAll();

    Optional<AttributeDto> findById(UUID id);

    void deleteById(UUID id);

}
