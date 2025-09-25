package fava.betaja.erp.service.baseinfo;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FileStorageDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileStorageService {

    FileStorageDto save(String recordId, MultipartFile file, String description, String key);

    FileStorageDto update(UUID id, MultipartFile file);

    PageResponse<FileStorageDto> findAll(PageRequest<FileStorageDto> model);

    Optional<FileStorageDto> getById(UUID id);

    Optional<FileStorageDto> getByFileCode(String fileCode);

    Optional<FileStorageDto> findByRecordIdAndKey(String recordId, String key);

    List<FileStorageDto> getFilesDtoByByRecordId(String recordId);

    Resource getFileAsResource(String fileName);

    Boolean deleteById(UUID id);


}
