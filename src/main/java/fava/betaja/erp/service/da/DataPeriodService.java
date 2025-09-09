package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.DataPeriodDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataPeriodService {

    DataPeriodDto save(DataPeriodDto dataPeriodDto);

    DataPeriodDto update(DataPeriodDto dataPeriodDto);

    PageResponse<DataPeriodDto> findAll(PageRequest<DataPeriodDto> model);

    List<DataPeriodDto> findAll();

    Optional<DataPeriodDto> findById(UUID id);

    Boolean deleteById(UUID id);

}
