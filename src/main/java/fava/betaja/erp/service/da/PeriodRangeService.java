package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.PeriodRangeDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PeriodRangeService {

    PeriodRangeDto save(PeriodRangeDto periodRangeDto);

    PeriodRangeDto update(PeriodRangeDto periodRangeDto);

    PageResponse<PeriodRangeDto> findAll(PageRequest<PeriodRangeDto> model);

    List<PeriodRangeDto> findAll();

    Optional<PeriodRangeDto> findById(UUID id);

    void deleteById(UUID id);

}
