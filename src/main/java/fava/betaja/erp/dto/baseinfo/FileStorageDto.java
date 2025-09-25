package fava.betaja.erp.dto.baseinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileStorageDto {

    private UUID id;
    private String filesPath;
    private String fileType;
    private String key;
    private String recordId;
    private String fileCode;
    private String fileName;
    private Long size;
    private String description;
}