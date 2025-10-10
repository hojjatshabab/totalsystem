package fava.betaja.erp.service.baseinfo;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableHistoryDto;
import fava.betaja.erp.entities.baseinfo.CartableHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartableHistoryService {

    CartableHistoryDto save(CartableHistoryDto historyDto);

    PageResponse<CartableHistoryDto> findAll(PageRequest<CartableHistoryDto> model);

    List<CartableHistoryDto> findByCartableIdOrderByCreationDateTimeDesc(UUID cartableId);

    List<CartableHistoryDto> findByCartableIdOrderByCreationDateTimeAsc(UUID cartableId);

    List<CartableHistoryDto> findAll();

    Optional<CartableHistoryDto> findById(UUID id);

    void deleteById(UUID id);

}

