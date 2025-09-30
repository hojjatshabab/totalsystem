package fava.betaja.erp.mapper.security;


import fava.betaja.erp.dto.security.UsersDto;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UsersDtoMapper extends BaseMapper<UsersDto, Users> {

    @Override
    @Mapping(target = "organizationUnit.id", source = "organizationUnitId")
    Users toEntity(UsersDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Users> toEntityList(List<UsersDto> dtoList);

    @Override
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    UsersDto toDto(Users entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<UsersDto> toDtoList(List<Users> entityList);
}