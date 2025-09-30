package fava.betaja.erp.mapper.security;

import fava.betaja.erp.dto.security.PermissionDto;
import fava.betaja.erp.entities.security.Permission;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PermissionDtoMapper extends BaseMapper<PermissionDto, Permission> {

    @Override
    Permission toEntity(PermissionDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Permission> toEntityList(List<PermissionDto> dtoList);

    @Override
    PermissionDto toDto(Permission entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<PermissionDto> toDtoList(List<Permission> entityList);
}
