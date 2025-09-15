package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.AttributeValueDto;
import fava.betaja.erp.entities.da.AttributeValue;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AttributeValueDtoMapper extends BaseMapper<AttributeValueDto, AttributeValue> {

    @Mapping(target = "attributePeriod.id", source = "attributePeriodId")
    @Mapping(target = "periodRange.id", source = "periodRangeId")
    @Mapping(target = "attributePeriod.attribute.id", source = "attributeId")
    @Mapping(target = "attributePeriod.attribute.organizationUnit.id", source = "organizationId")
    AttributeValue toEntity(AttributeValueDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<AttributeValue> toEntityList(List<AttributeValueDto> dtoList);

    @Override
    @Mapping(source = "attributePeriod.id", target = "attributePeriodId")
    @Mapping(source = "attributePeriod.title", target = "attributePeriodTitle")
    @Mapping(source = "periodRange.id", target = "periodRangeId")
    @Mapping(source = "periodRange.name", target = "periodRangeName")
    @Mapping(source = "attributePeriod.attribute.id", target = "attributeId")
    @Mapping(source = "attributePeriod.attribute.name", target = "attributeName")
    @Mapping(source = "attributePeriod.attribute.organizationUnit.id", target = "organizationId")
    @Mapping(source = "attributePeriod.attribute.organizationUnit.name", target = "organizationName")
    @Mapping(source = "attributePeriod.valuePlanned", target = "valuePlanned")
    AttributeValueDto toDto(AttributeValue entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<AttributeValueDto> toDtoList(List<AttributeValue> entityList);

}
