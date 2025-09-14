package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.AttributePeriodDto;
import fava.betaja.erp.entities.da.AttributePeriod;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AttributePeriodDtoMapper extends BaseMapper<AttributePeriodDto, AttributePeriod> {

    @Mapping(target = "attribute.id", source = "attributeId")
    @Mapping(target = "periodRange.id", source = "periodRangeId")
    @Mapping(target = "attribute.organizationUnit.id", source = "organizationId")
    AttributePeriod toEntity(AttributePeriodDto dto);


    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<AttributePeriod> toEntityList(List<AttributePeriodDto> dtoList);

    @Override
    @Mapping(source = "attribute.id", target = "attributeId")
    @Mapping(source = "attribute.name", target = "attributeName")
    @Mapping(source = "periodRange.id", target = "periodRangeId")
    @Mapping(source = "periodRange.name", target = "periodRangeName")
    @Mapping(source = "attribute.organizationUnit.id", target = "organizationId")
    @Mapping(source = "attribute.organizationUnit.name", target = "organizationName")
    AttributePeriodDto toDto(AttributePeriod entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<AttributePeriodDto> toDtoList(List<AttributePeriod> entityList);

}
