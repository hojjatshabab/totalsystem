package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.ProgressValueDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgressValueService {

    ProgressValueDto save(ProgressValueDto progressValueDto);

    ProgressValueDto update(ProgressValueDto progressValueDto);

    PageResponse<ProgressValueDto> findAll(PageRequest<ProgressValueDto> model);

    PageResponse<ProgressValueDto> findByProgressValueId(UUID progressValueId, PageRequest<ProgressValueDto> model);

    List<ProgressValueDto> findAll();

    Optional<ProgressValueDto> findById(UUID id);

    void deleteById(UUID id);

}
