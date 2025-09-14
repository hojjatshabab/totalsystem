package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.AttributeDto;
import fava.betaja.erp.entities.da.Attribute;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AttributeDtoMapper extends BaseMapper<AttributeDto, Attribute> {

    @Mapping(target = "organizationUnit.id", source = "organizationUnitId")
    Attribute toEntity(AttributeDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Attribute> toEntityList(List<AttributeDto> dtoList);

    @Override
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    AttributeDto toDto(Attribute entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<AttributeDto> toDtoList(List<Attribute> entityList);
}
