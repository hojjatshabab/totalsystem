package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.ProgressPeriodDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgressPeriodService {

    ProgressPeriodDto save(ProgressPeriodDto progressPeriodDto);

    ProgressPeriodDto update(ProgressPeriodDto progressPeriodDto);

    PageResponse<ProgressPeriodDto> findAll(PageRequest<ProgressPeriodDto> model);

    PageResponse<ProgressPeriodDto> findByReferenceId(UUID referenceId, PageRequest<ProgressPeriodDto> model);

    BigDecimal getTotalValue(UUID progressPeriodId);

    List<ProgressPeriodDto> findAll();

    Optional<ProgressPeriodDto> findById(UUID id);

    void deleteById(UUID id);

}
