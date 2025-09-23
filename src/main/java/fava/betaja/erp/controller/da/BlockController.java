package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.BlockDto;
import fava.betaja.erp.dto.da.ProjectDto;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.service.da.BlockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/block")
@Tag(name = "بلوک ها", description = "مدیریت بلوک های هر پروژه")
public class BlockController extends BaseController {

    private final BlockService service;

    @PostMapping
    public ActionResult<BlockDto> save(@RequestBody @Valid BlockDto dto, Locale locale) {
        return RESULT(service.save(dto), locale);
    }

    @PutMapping
    public ActionResult<BlockDto> update(@RequestBody @Valid BlockDto dto, Locale locale) {
        if (dto.getId() == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(dto.getId())
                .orElseThrow(() -> new ServiceException("Block با این id یافت نشد: " + dto.getId()));
        return RESULT(service.update(dto), locale);
    }

    @GetMapping
    public ActionResult<PageResponse<BlockDto>> findAll(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<BlockDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<BlockDto> response = service.findAll(request);
        return RESULT(response, locale);
    }

    @GetMapping("find-by-project")
    public ActionResult<PageResponse<BlockDto>> findByProjectId(@RequestParam int currentPage,
                                                                             @RequestParam int pageSize,
                                                                             @RequestParam UUID projectId,
                                                                             Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE("{ currentPage <= 0 || pageSize <= 0 }", locale);
        }
        PageRequest<BlockDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<BlockDto> response = service.findByProjectId(projectId, request);
        return RESULT(response, locale);
    }

    @GetMapping("/list")
    public ActionResult<List<BlockDto>> list(Locale locale) {
        List<BlockDto> result = service.findAll();
        return RESULT(result, locale);
    }

    @GetMapping("/id/{id}")
    public ActionResult<BlockDto> findById(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        BlockDto dto = service.findById(id)
                .orElseThrow(() -> new ServiceException("Block با این id یافت نشد: " + id));
        return RESULT(dto, locale);
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id == null) {
            return NO_CONTENT("id", locale);
        }
        service.findById(id)
                .orElseThrow(() -> new ServiceException("Block با این id یافت نشد: " + id));
        service.deleteById(id);
        return RESULT(true, locale);
    }
}
