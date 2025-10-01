package fava.betaja.erp.controller.baseinfo;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.baseinfo.CartableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cartable")
@Tag(name = "کارتابل", description = "مدیریت کارتابل‌ها")
public class CartableController extends BaseController {

    private final CartableService cartableService;

    @PostMapping
    public ActionResult<CartableDto> save(@RequestBody @Valid CartableDto cartableDto, Locale locale) {
        return RESULT(cartableService.save(cartableDto), locale);
    }

    @PutMapping
    public ActionResult<CartableDto> update(@RequestBody @Valid CartableDto cartableDto, Locale locale) {
        if (cartableDto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        cartableService.findById(cartableDto.getId())
                .orElseThrow(() -> new ServiceException("Cartable با این id یافت نشد: " + cartableDto.getId()));
        return RESULT(cartableService.update(cartableDto), locale);
    }

    @PutMapping("/next-step-cartable")
    public ActionResult<CartableDto> nextStepCartable(@RequestBody @Valid CartableDto cartableDto, Locale locale) {
        if (cartableDto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        cartableService.findById(cartableDto.getId())
                .orElseThrow(() -> new ServiceException("Cartable با این id یافت نشد: " + cartableDto.getId()));
        return RESULT(cartableService.nextStepCartable(cartableDto), locale);
    }

    @PutMapping("/accept-cartable")
    public ActionResult<CartableDto> acceptCartable(@RequestBody @Valid CartableDto cartableDto, Locale locale) {
        if (cartableDto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        cartableService.findById(cartableDto.getId())
                .orElseThrow(() -> new ServiceException("Cartable با این id یافت نشد: " + cartableDto.getId()));
        return RESULT(cartableService.acceptCartable(cartableDto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<CartableDto>> findAll(@RequestParam int currentPage,
                                                           @RequestParam int pageSize,
                                                           Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<CartableDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<CartableDto> response = cartableService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<CartableDto>> list(Locale locale) {
        return RESULT(cartableService.findAll(), locale);
    }

    @GetMapping("/recipient/{recipientId}")
    public ActionResult<List<CartableDto>> findByRecipientId(@PathVariable Long recipientId, Locale locale) {
        if (recipientId == null) {
            return NO_CONTENT("recipientId", locale);
        }
        return RESULT(cartableService.findByRecipientIdOrderBySendDateDesc(recipientId), locale);
    }

    @GetMapping("/step/{currentStepId}")
    public ActionResult<List<CartableDto>> findByCurrentStepId(@PathVariable UUID currentStepId, Locale locale) {
        if (currentStepId == null) {
            return NO_CONTENT("currentStepId", locale);
        }
        return RESULT(cartableService.findByCurrentStepId(currentStepId), locale);
    }

    @GetMapping("/state/{state}")
    public ActionResult<List<CartableDto>> findByState(@PathVariable String state, Locale locale) {
        if (state == null || state.isBlank()) {
            return NO_CONTENT("state", locale);
        }
        return RESULT(cartableService.findByState(state), locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<CartableDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        CartableDto dto = cartableService.findById(id)
                .orElseThrow(() -> new ServiceException("Cartable با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        cartableService.findById(id)
                .orElseThrow(() -> new ServiceException("Cartable با این id یافت نشد: " + id));
        cartableService.deleteById(id);
        return RESULT(true, locale);
    }
}
