package fava.betaja.erp.mapper.baseinfo;

import fava.betaja.erp.dto.baseinfo.FileStorageDto;
import fava.betaja.erp.entities.baseinfo.FileStorage;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FileStorageDtoMapper extends BaseMapper<FileStorageDto, FileStorage> {

    @Override
    FileStorage toEntity(FileStorageDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<FileStorage> toEntityList(List<FileStorageDto> dtoList);

    @Override
    FileStorageDto toDto(FileStorage entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<FileStorageDto> toDtoList(List<FileStorage> entityList);
}
