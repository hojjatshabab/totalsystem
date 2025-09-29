package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockDto;
import fava.betaja.erp.dto.da.BlockValueDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.BlockValueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/block-value")
@Tag(name = "اطلاعات بلوک ها", description = "مدیریت اطلاعات هر بلوک")
public class BlockValueController extends BaseController {

    private final BlockValueService service;

    @PostMapping
    public ActionResult<BlockValueDto> save(@RequestBody @Valid BlockValueDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<BlockValueDto> update(@RequestBody @Valid BlockValueDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("BlockValue با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<BlockValueDto>> findAll(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<BlockValueDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<BlockValueDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-block-period")
    public ActionResult<PageResponse<BlockValueDto>> findByProjectId(@RequestParam int currentPage,
                                                                             @RequestParam int pageSize,
                                                                             @RequestParam UUID blockPeriodId,
                                                                             Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<BlockValueDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<BlockValueDto> response = service.findByBlockPeriodId(blockPeriodId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<BlockValueDto>> list(Locale locale) {
        List<BlockValueDto> result = service.findAll();
        return RESULT(result, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<BlockValueDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        BlockValueDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("BlockValue با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("BlockValue با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
