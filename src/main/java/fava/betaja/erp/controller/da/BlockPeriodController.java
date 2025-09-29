package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockPeriodDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.BlockPeriodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/block-period")
@Tag(name = "دوره های بلوک", description = "مدیریت دورهای بلوکه")
public class BlockPeriodController extends BaseController {

    private final BlockPeriodService service;

    @PostMapping
    public ActionResult<BlockPeriodDto> save(@RequestBody @Valid BlockPeriodDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<BlockPeriodDto> update(@RequestBody @Valid BlockPeriodDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("BlockPeriod با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<BlockPeriodDto>> findAll(@RequestParam int currentPage,
                                                              @RequestParam int pageSize,
                                                              Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<BlockPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<BlockPeriodDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-block")
    public ActionResult<PageResponse<BlockPeriodDto>> findByBlockId(@RequestParam int currentPage,
                                                                    @RequestParam int pageSize,
                                                                    @RequestParam UUID blockId,
                                                                    Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<BlockPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<BlockPeriodDto> response = service.findByBlockId(blockId, request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-block-range-year")
    public ActionResult<PageResponse<BlockPeriodDto>> findByBlockIdAndPeriodRangeIdAndYear(@RequestParam int currentPage,
                                                                                           @RequestParam int pageSize,
                                                                                           @RequestParam UUID blockId,
                                                                                           @RequestParam UUID periodRangeId,
                                                                                           @RequestParam String year,
                                                                                           Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<BlockPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<BlockPeriodDto> response = service.findByBlockIdAndPeriodRangeIdAndYear(blockId, periodRangeId, year, request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-range-year")
    public ActionResult<PageResponse<BlockPeriodDto>> findByPeriodRangeIdAndYear(@RequestParam int currentPage,
                                                                                 @RequestParam int pageSize,
                                                                                 @RequestParam UUID periodRangeId,
                                                                                 @RequestParam String year,
                                                                                 Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<BlockPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<BlockPeriodDto> response = service.findByPeriodRangeIdAndYear(periodRangeId,year, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<BlockPeriodDto>> list(Locale locale) {
        List<BlockPeriodDto> result = service.findAll();
        return RESULT(result, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<BlockPeriodDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        BlockPeriodDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("BlockPeriod با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("BlockPeriod با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
