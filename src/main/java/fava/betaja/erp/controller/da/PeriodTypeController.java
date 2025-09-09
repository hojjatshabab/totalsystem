package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.DataPeriodDto;
import fava.betaja.erp.dto.da.PeriodTypeDto;
import fava.betaja.erp.enums.ModeType;
import fava.betaja.erp.service.da.PeriodTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/period-type")
@Tag(name = "دوره های زمانی", description = "دوره های زمانی")
public class PeriodTypeController extends BaseController {

    private final PeriodTypeService periodTypeService;

    @PostMapping
    public ActionResult<PeriodTypeDto> save(@RequestBody PeriodTypeDto periodTypeDto, Locale locale) {
        isExist(periodTypeDto, ModeType.CREATE, locale);
        try {
            return RESULT(periodTypeService.update(periodTypeDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @PutMapping
    public ActionResult<PeriodTypeDto> update(@RequestBody PeriodTypeDto periodTypeDto, Locale locale) {
        isExist(periodTypeDto, ModeType.EDIT, locale);
        try {
            return RESULT(periodTypeService.update(periodTypeDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping
    public ActionResult<PageResponse<PeriodTypeDto>> findAll(@RequestParam int currentPage, @RequestParam int pageSize, Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        PageRequest<PeriodTypeDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<PeriodTypeDto> periodTypeDtoPageResponse;
        try {
            periodTypeDtoPageResponse = periodTypeService.findAll(request);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (Objects.isNull(periodTypeDtoPageResponse)) {
            return NO_CONTENT("periodTypeDtoPageResponse", locale);
        } else {
            return RESULT(periodTypeDtoPageResponse, locale);
        }
    }

    @GetMapping("/list")
    public ActionResult<List<PeriodTypeDto>> list(Locale locale) {
        try {
            return RESULT(periodTypeService.findAll(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/id/{id}")
    public ActionResult<Optional<PeriodTypeDto>> findById(@PathVariable UUID id, Locale locale) {
        Optional<PeriodTypeDto> periodTypeDto;
        if (id.equals(null)) {
            return NO_CONTENT(" id= " + id, locale);
        }
        try {
            periodTypeDto = periodTypeService.findById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (periodTypeDto.isPresent()) {
            return RESULT(periodTypeDto, locale);
        } else {
            return NOT_FOUND(" id= " + id, locale);
        }
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!periodTypeService.findById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        try {
            return RESULT(periodTypeService.deleteById(id), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    private void isExist(PeriodTypeDto periodTypeDto, ModeType modeType, Locale locale) {
        if (modeType.equals(ModeType.EDIT)) {
            if (Objects.nonNull(periodTypeDto.getId())) {
                Optional<PeriodTypeDto> optionalAttributeDto = periodTypeService.findById(periodTypeDto.getId());
                if (!optionalAttributeDto.isPresent()) {
                    NOT_FOUND(" id ", locale);
                }
            } else {
                NO_CONTENT(" id ", locale);
            }
        }
        if (Objects.isNull(periodTypeDto.getName()) || periodTypeDto.getName().isEmpty()) {
            NO_CONTENT(" name ", locale);
        }
    }
}
