package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.AttributeValueDto;
import fava.betaja.erp.entities.da.AttributeValue;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        , componentModel = "spring")
public interface AttributeValueDtoMapper extends BaseMapper<AttributeValueDto, AttributeValue> {

    @Override
    @Mapping(source = "organizationUnitId", target = "attribute.organizationUnit.id")
    @Mapping(source = "organizationUnitName", target = "attribute.organizationUnit.name")
    @Mapping(source = "attributeId", target = "attribute.id")
    @Mapping(source = "attributeName", target = "attribute.name")
    @Mapping(source = "dataPeriodId", target = "dataPeriod.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AttributeValue toEntity(AttributeValueDto dto);

    @Override
    @Mapping(source = "attribute.organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "attribute.organizationUnit.name", target = "organizationUnitName")
    @Mapping(source = "attribute.id", target = "attributeId")
    @Mapping(source = "attribute.name", target = "attributeName")
    @Mapping(source = "dataPeriod.id", target = "dataPeriodId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<AttributeValueDto> toDtoList(List<AttributeValue> entityList);

    @Override
    @Mapping(source = "organizationUnitId", target = "attribute.organizationUnit.id")
    @Mapping(source = "organizationUnitName", target = "attribute.organizationUnit.name")
    @Mapping(source = "attributeId", target = "attribute.id")
    @Mapping(source = "attributeName", target = "attribute.name")
    @Mapping(source = "dataPeriodId", target = "dataPeriod.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<AttributeValue> toEntityList(List<AttributeValueDto> dtoList);

    @Override
    @Mapping(source = "attribute.organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "attribute.organizationUnit.name", target = "organizationUnitName")
    @Mapping(source = "attribute.id", target = "attributeId")
    @Mapping(source = "attribute.name", target = "attributeName")
    @Mapping(source = "dataPeriod.id", target = "dataPeriodId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AttributeValueDto toDto(AttributeValue entity);


}
