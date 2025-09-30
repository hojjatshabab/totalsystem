package fava.betaja.erp.mapper.security;

import fava.betaja.erp.dto.security.RoleDto;
import fava.betaja.erp.entities.security.Role;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {RolePermissionDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RoleDtoMapper extends BaseMapper<RoleDto, Role> {

    @Override
    @Mapping(target = "rolePermissions", source = "rolePermissions")
    Role toEntity(RoleDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Role> toEntityList(List<RoleDto> dtoList);

    @Override
    @Mapping(source = "rolePermissions", target = "rolePermissions")
    RoleDto toDto(Role entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<RoleDto> toDtoList(List<Role> entityList);
}
