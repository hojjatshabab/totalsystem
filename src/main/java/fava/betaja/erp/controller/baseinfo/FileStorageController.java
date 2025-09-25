package fava.betaja.erp.controller.baseinfo;

import fava.betaja.erp.controller.ActionResult;
import fava.betaja.erp.controller.BaseController;
import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FileStorageDto;
import fava.betaja.erp.enums.ModeType;
import fava.betaja.erp.service.baseinfo.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("api/file-storages")
@RequiredArgsConstructor
public class FileStorageController extends BaseController {

    private final FileStorageService fileStorageService;
    private final MessageSource messageSource;

    @PostMapping("upload-file/{recordId}")
    public ActionResult<FileStorageDto> save(@PathVariable String recordId, @RequestParam("file") MultipartFile file
            , @RequestParam("description") String description, @RequestParam(required = false) String key, Locale locale) {
        isExist(null, recordId, ModeType.CREATE, file, locale);
        if (Objects.isNull(file))
            return CONFLICT("file is empty", locale);
        try {
            if (Objects.isNull(key)) {
                key = RandomStringUtils.randomAlphanumeric(8);
            }
            return RESULT(fileStorageService.save(recordId, file, description, key), locale);
        } catch (DataIntegrityViolationException exception) {
            return CONFLICT("recordId", locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @PutMapping("upload-file/{id}")
    public ActionResult<FileStorageDto> update(@PathVariable UUID id, @RequestParam("file") MultipartFile file, Locale locale) {
        isExist(id, null, ModeType.EDIT, file, locale);
        if (Objects.isNull(file)) return CONFLICT("file is empty", locale);
        try {
            return RESULT(fileStorageService.update(id, file), locale);
        } catch (DataIntegrityViolationException exception) {
            return CONFLICT("recordId", locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping
    public ActionResult<PageResponse<FileStorageDto>> findAll(
            @RequestParam int currentPage,
            @RequestParam int pageSize,
            Locale locale) {
        if (currentPage <= 0 || pageSize <= 0) {
            return NOT_ACCEPTABLE(" { currentPage == 0 || pageSize == 0 } ", locale);
        }
        PageRequest<FileStorageDto> request = new PageRequest<>();
        request.setPageSize(pageSize);
        request.setCurrentPage(currentPage);
        PageResponse<FileStorageDto> fileDtoPageResponse;
        try {
            fileDtoPageResponse = fileStorageService.findAll(request);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (Objects.isNull(fileDtoPageResponse)) {
            return NO_CONTENT("FileStorage", locale);
        } else {
            return RESULT(fileDtoPageResponse, locale);
        }
    }

    @GetMapping("/id/{id}")
    public ActionResult<Optional<FileStorageDto>> findById(@PathVariable UUID id, Locale locale) {
        Optional<FileStorageDto> optionalFileDto;
        if (id.equals(null)) {
            return NO_CONTENT(" id= " + id, locale);
        }
        try {
            optionalFileDto = fileStorageService.getById(id);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
        if (optionalFileDto.isPresent()) {
            return RESULT(optionalFileDto, locale);
        } else {
            return NOT_FOUND(" id= " + id, locale);
        }
    }

    @GetMapping("/find-all-file-by-record-id/{recordId}")
    public ActionResult<List<FileStorageDto>> findAllFileByRecordId(@PathVariable String recordId, Locale locale) {
        if (recordId.equals(null)) {
            return NO_CONTENT(" id= " + recordId, locale);
        }
        try {
            return RESULT(fileStorageService.getFilesDtoByByRecordId(recordId), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/find-by-record-id-and-key")
    public ActionResult<Optional<FileStorageDto>> findByRecordIdAndKey(@RequestParam String recordId
            , @RequestParam String key, Locale locale) {
        try {
            return RESULT(fileStorageService.findByRecordIdAndKey(recordId, key), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    @GetMapping("/download-file-by-file-code/{fileCode}")
    public ResponseEntity<?> downloadFileByFileCode(@PathVariable String fileCode, Locale locale) {

        Resource resource = null;
        try {
            resource = fileStorageService.getFileAsResource(fileCode);
            if (resource == null) {
                String message = messageSource.getMessage("error.notfound", null, locale);
                return new ResponseEntity<>(message + "{ file not found } ", HttpStatus.NOT_FOUND);
            }
            Optional<FileStorageDto> optionalFileStorageDto = fileStorageService.getByFileCode(fileCode);
            String contentType = optionalFileStorageDto.get().getFileType();
            String headerValue = "attachment; filename=\"" + optionalFileStorageDto.get().getFileName() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download-file-by-file-id/{id}")
    public ResponseEntity<?> downloadFileByFileId(@PathVariable UUID id, Locale locale) {
        Optional<FileStorageDto> optionalFileDto;
        if (id.equals(null)) {
            return new ResponseEntity<>("ID_NO_CONTENT", HttpStatus.NO_CONTENT);
        }
        try {
            optionalFileDto = fileStorageService.getById(id);
        } catch (Exception exception) {
            return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (optionalFileDto.isPresent()) {
            return downloadFileByFileCode(optionalFileDto.get().getFileCode(), locale);
        } else {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ActionResult<Boolean> delete(@PathVariable UUID id, Locale locale) {
        if (id.equals(null)) {
            return NO_CONTENT("id", locale);
        }
        if (!fileStorageService.getById(id).isPresent()) {
            return NOT_FOUND("id =" + id, locale);
        }
        try {
            return RESULT(fileStorageService.deleteById(id), locale);
        } catch (Exception exception) {
            return INTERNAL_SERVER_ERROR(exception.getMessage(), locale);
        }
    }

    private void isExist(UUID id, String recordId, ModeType modeType, MultipartFile file, Locale locale) {
        if (modeType.equals(ModeType.CREATE)) {
            isExistByStringId(recordId, locale);
            List<FileStorageDto> fileStorageDtoList = fileStorageService.getFilesDtoByByRecordId(recordId);
            if (!fileStorageDtoList.isEmpty())
                for (FileStorageDto dto : fileStorageDtoList) {
                    if (dto.getFileName().equals(file.getOriginalFilename()))
                        CONFLICT("The file is available by recordId ".concat(recordId.toString()), locale);
                }
        }
        if (modeType.equals(ModeType.EDIT)) {
            isExistByUUIDId(id, locale);
            Optional<FileStorageDto> optionalFileDto = fileStorageService.getById(id);
            if (!optionalFileDto.isPresent())
                CONFLICT("id", locale);
            else {
                List<FileStorageDto> fileStorageDtoList = fileStorageService.getFilesDtoByByRecordId(optionalFileDto.get().getRecordId());
                if (!fileStorageDtoList.isEmpty())
                    for (FileStorageDto dto : fileStorageDtoList) {
                        if (dto.getFileName().equals(file.getOriginalFilename()))
                            CONFLICT("The file is available by id ".concat(id.toString()), locale);
                    }
            }
        }

    }

    private void isExistByStringId(String id, Locale locale) {
        if (Objects.isNull(id) || id.isEmpty() || id.isBlank()) {
            NO_CONTENT("id =" + id.toString(), locale);
        }
    }

    private void isExistByUUIDId(UUID id, Locale locale) {
        if (Objects.isNull(id)) {
            NO_CONTENT("id =" + id.toString(), locale);
        }
    }
}

