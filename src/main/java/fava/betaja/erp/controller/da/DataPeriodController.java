package fava.betaja.erp.controller.da;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.dto.da.DataPeriodDto;
import fava.betaja.erp.enums.ModeType;
import fava.betaja.erp.service.da.DataPeriodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/data-period")
@Tag(name = "دیتای دوره زمانی", description = "دیتای هر دوره زمانی")
public class DataPeriodController extends BaseController {

    private final DataPeriodService dataPeriodService;

    @PostMapping
    public ActionResult<DataPeriodDto> save(@RequestBody DataPeriodDto dataPeriodDto, Locale locale) {
        isExist(dataPeriodDto, ModeType.CREATE, locale);
        try {
            return RESULT(dataPeriodService.update(dataPeriodDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @PutMapping
    public ActionResult<DataPeriodDto> update(@RequestBody DataPeriodDto dataPeriodDto, Locale locale) {
        isExist(dataPeriodDto, ModeType.EDIT, locale);
        try {
            return RESULT(dataPeriodService.update(dataPeriodDto), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping
    public ActionResult<PageResponse<DataPeriodDto>> findAll(@RequestParam int currentPage, @RequestParam int pageSize, Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        PageRequest<DataPeriodDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<DataPeriodDto> dataPeriodDtoPageResponse;
        try {
            dataPeriodDtoPageResponse = dataPeriodService.findAll(request);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (Objects.isNull(dataPeriodDtoPageResponse)) {
            return NO_CONTENT("dataPeriodDtoPageResponse", locale);
        } else {
            return RESULT(dataPeriodDtoPageResponse, locale);
        }
    }

    @GetMapping("/list")
    public ActionResult<List<DataPeriodDto>> list(Locale locale) {
        try {
            return RESULT(dataPeriodService.findAll(), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/id/{id}")
    public ActionResult<Optional<DataPeriodDto>> findById(@PathVariable UUID id, Locale locale) {
        Optional<DataPeriodDto> optionalDataPeriodDto;
        if (id.equals(null)) {
            return NO_CONTENT(" id= " + id, locale);
        }
        try {
            optionalDataPeriodDto = dataPeriodService.findById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalDataPeriodDto.isPresent()) {
            return RESULT(optionalDataPeriodDto, locale);
        } else {
            return NOT_FOUND(" id= " + id, locale);
        }
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!dataPeriodService.findById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        try {
            return RESULT(dataPeriodService.deleteById(id), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    private void isExist(DataPeriodDto dataPeriodDto, ModeType modeType, Locale locale) {
        if (modeType.equals(ModeType.EDIT)) {
            if (Objects.nonNull(dataPeriodDto.getId())) {
                Optional<DataPeriodDto> optionalAttributeDto = dataPeriodService.findById(dataPeriodDto.getId());
                if (!optionalAttributeDto.isPresent()) {
                    NOT_FOUND(" id ", locale);
                }
            } else {
                NO_CONTENT(" id ", locale);
            }
        }
        if (Objects.isNull(dataPeriodDto.getStartDate())) {
            NO_CONTENT(" startDate ", locale);
        }
    }
}
