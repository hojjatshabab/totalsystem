package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributePeriodDto;
import fava.betaja.erp.entities.da.AttributePeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributePeriodService {

    AttributePeriodDto save(AttributePeriodDto attributePeriodDto);

    AttributePeriodDto update(AttributePeriodDto attributePeriodDto);

    PageResponse<AttributePeriodDto> findAll(PageRequest<AttributePeriodDto> model);

    PageResponse<AttributePeriodDto> findByAttributeId(UUID attributeId,PageRequest<AttributePeriodDto> model);

    BigDecimal getTotalValue(UUID attributePeriodId);

    List<AttributePeriodDto> findAll();

    Optional<AttributePeriodDto> findById(UUID id);

    void deleteById(UUID id);

}
