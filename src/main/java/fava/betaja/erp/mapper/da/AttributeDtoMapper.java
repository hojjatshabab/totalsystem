package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.entities.da.Attribute;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        , componentModel = "spring")
public interface AttributeDtoMapper extends BaseMapper<AttributeDto, Attribute> {

    @Override
    @Mapping(source = "organizationUnitId", target = "organizationUnit.id")
    @Mapping(source = "organizationUnitName", target = "organizationUnit.name")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Attribute toEntity(AttributeDto dto);

    @Override
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<AttributeDto> toDtoList(List<Attribute> entityList);

    @Override
    @Mapping(source = "organizationUnitId", target = "organizationUnit.id")
    @Mapping(source = "organizationUnitName", target = "organizationUnit.name")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<Attribute> toEntityList(List<AttributeDto> dtoList);

    @Override
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AttributeDto toDto(Attribute entity);


}
