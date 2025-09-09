package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.OrganizationalPerformanceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeService {

    AttributeDto save(AttributeDto attributeDto);

    AttributeDto update(AttributeDto attributeDto);

    PageResponse<AttributeDto> findAll(PageRequest<AttributeDto> model);

    List<AttributeDto> findAll();

    Optional<AttributeDto> findById(UUID id);

    Boolean deleteById(UUID id);

}
