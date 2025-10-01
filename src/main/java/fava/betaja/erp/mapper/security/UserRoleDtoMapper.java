package fava.betaja.erp.mapper.security;

import fava.betaja.erp.dto.security.UserRoleDto;
import fava.betaja.erp.entities.security.UserRole;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserRoleDtoMapper extends BaseMapper<UserRoleDto, UserRole> {

    @Override
    @Mapping(target = "role.id", source = "roleId")
    @Mapping(target = "user.id", source = "userId")
    UserRole toEntity(UserRoleDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<UserRole> toEntityList(List<UserRoleDto> dtoList);

    @Override
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "role.title", target = "roleTitle")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.lastname", target = "lastname")
    UserRoleDto toDto(UserRole entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<UserRoleDto> toDtoList(List<UserRole> entityList);
}
