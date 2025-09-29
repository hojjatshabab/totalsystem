package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockDto;
import fava.betaja.erp.dto.da.BlockPeriodDto;
import fava.betaja.erp.entities.da.BlockPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlockPeriodService {

    BlockPeriodDto save(BlockPeriodDto dto);

    BlockPeriodDto update(BlockPeriodDto dto);

    PageResponse<BlockPeriodDto> findAll(PageRequest<BlockPeriodDto> model);

    PageResponse<BlockPeriodDto> findByBlockId(UUID blockId, PageRequest<BlockPeriodDto> model);

    PageResponse<BlockPeriodDto> findByBlockIdAndPeriodRangeIdAndYear(UUID blockId, UUID periodId, String year, PageRequest<BlockPeriodDto> model);

    PageResponse<BlockPeriodDto> findByPeriodRangeIdAndYear(UUID periodId, String year, PageRequest<BlockPeriodDto> model);

    List<BlockPeriodDto> findAll();

    Optional<BlockPeriodDto> findById(UUID id);

    void deleteById(UUID id);

}
