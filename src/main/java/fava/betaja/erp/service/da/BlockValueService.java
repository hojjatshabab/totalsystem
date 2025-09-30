package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockValueDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlockValueService {

    BlockValueDto save(BlockValueDto dto);

    BlockValueDto update(BlockValueDto dto);

    PageResponse<BlockValueDto> findAll(PageRequest<BlockValueDto> model);

    PageResponse<BlockValueDto> findByProjectPeriodId(UUID projectPeriodId, PageRequest<BlockValueDto> model);

    PageResponse<BlockValueDto> findByProjectPeriodIdAndBlockId(UUID projectPeriodId, UUID blockId, PageRequest<BlockValueDto> model);

    PageResponse<BlockValueDto> findByBlockId(UUID blockId, PageRequest<BlockValueDto> model);

    List<BlockValueDto> findAll();

    Optional<BlockValueDto> findById(UUID id);

    void deleteById(UUID id);

}
