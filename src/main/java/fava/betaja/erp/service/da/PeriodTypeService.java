package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.DataPeriodDto;
import fava.betaja.erp.dto.da.PeriodTypeDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PeriodTypeService {

    PeriodTypeDto save(PeriodTypeDto periodTypeDto);

    PeriodTypeDto update(PeriodTypeDto periodTypeDto);

    PageResponse<PeriodTypeDto> findAll(PageRequest<PeriodTypeDto> model);

    List<PeriodTypeDto> findAll();

    Optional<PeriodTypeDto> findById(UUID id);

    Boolean deleteById(UUID id);

}
