package fava.betaja.erp.controller.baseinfo;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableHistoryDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.baseinfo.CartableHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cartable-history")
@Tag(name = "تاریخچه کارتابل", description = "مدیریت تاریخچه تغییرات کارتابل")
public class CartableHistoryController extends BaseController {

    private final CartableHistoryService cartableHistoryService;

    @GetMapping
    public ActionResult<PageResponse<CartableHistoryDto>> findAll(@RequestParam int currentPage,
                                                                  @RequestParam int pageSize,
                                                                  Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<CartableHistoryDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<CartableHistoryDto> response = cartableHistoryService.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<CartableHistoryDto>> list(Locale locale) {
        return RESULT(cartableHistoryService.findAll(), locale);
    }

    @GetMapping("/workflow")
    public ActionResult<List<CartableHistoryDto>> workflow(@RequestParam UUID cartableId, Locale locale) {
        return RESULT(cartableHistoryService.findByCartableIdOrderByCreationDateTimeAsc(cartableId), locale);
    }

    @GetMapping("/cartable/{cartableId}")
    public ActionResult<List<CartableHistoryDto>> findByCartableId(@PathVariable UUID cartableId, Locale locale) {
        if (cartableId == null) {
            return NO_CONTENT("cartableId", locale);
        }
        return RESULT(cartableHistoryService.findByCartableIdOrderByCreationDateTimeDesc(cartableId), locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<CartableHistoryDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        CartableHistoryDto dto = cartableHistoryService.findById(id)
                .orElseThrow(() -> new ServiceException("CartableHistory با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }
}

