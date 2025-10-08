package fava.betaja.erp.service.baseinfo;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.enums.baseinfo.CartableState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartableService {

    CartableDto save(CartableDto cartableDto);

    CartableDto update(CartableDto cartableDto);

    CartableDto nextStepCartable(CartableDto cartableDto);

    CartableDto cartableToNextStep(UUID cartableId, String comment);

    CartableDto acceptCartable(CartableDto cartableDto);

    PageResponse<CartableDto> findAll(PageRequest<CartableDto> model);

    PageResponse<CartableDto> findByStatePage(CartableState state, PageRequest<CartableDto> model);

    List<CartableDto> findAll();

    List<CartableDto> findByRecipientIdOrderBySendDateDesc(Long recipientId);

    List<CartableDto> findByCurrentStepId(UUID currentStepId);

    List<CartableDto> findByStatePage(String state);

    Optional<CartableDto> findById(UUID id);

    void deleteById(UUID id);

}
