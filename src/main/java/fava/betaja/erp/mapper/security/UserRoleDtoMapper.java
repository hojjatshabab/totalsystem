package fava.betaja.erp.mapper.security;

import fava.betaja.erp.dto.security.RolePermissionDto;
import fava.betaja.erp.entities.security.RolePermission;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserRoleDtoMapper extends BaseMapper<RolePermissionDto, RolePermission> {

    @Override
    @Mapping(target = "role.id", source = "roleId")
    @Mapping(target = "permission.id", source = "permissionId")
    RolePermission toEntity(RolePermissionDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<RolePermission> toEntityList(List<RolePermissionDto> dtoList);

    @Override
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "permission.id", target = "permissionId")
    @Mapping(source = "permission.name", target = "permissionName")
    RolePermissionDto toDto(RolePermission entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<RolePermissionDto> toDtoList(List<RolePermission> entityList);
}
