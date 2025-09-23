package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockDto;
import fava.betaja.erp.dto.da.ProjectDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlockService {

    BlockDto save(BlockDto dto);

    BlockDto update(BlockDto dto);

    PageResponse<BlockDto> findAll(PageRequest<BlockDto> model);

    PageResponse<BlockDto> findByProjectId(UUID projectId, PageRequest<BlockDto> model);

    List<BlockDto> findAll();

    Optional<BlockDto> findById(UUID id);

    void deleteById(UUID id);

}
