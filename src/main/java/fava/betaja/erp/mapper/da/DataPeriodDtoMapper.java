package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.DataPeriodDto;
import fava.betaja.erp.entities.da.DataPeriod;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        , componentModel = "spring")
public interface DataPeriodDtoMapper extends BaseMapper<DataPeriodDto, DataPeriod> {

    @Override
    @Mapping(source = "organizationUnitId", target = "organizationUnit.id")
    @Mapping(source = "organizationUnitName", target = "organizationUnit.name")
    @Mapping(source = "periodTypeId", target = "periodType.id")
    @Mapping(source = "periodTypeName", target = "periodType.name")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DataPeriod toEntity(DataPeriodDto dto);

    @Override
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    @Mapping(source = "periodType.id", target = "periodTypeId")
    @Mapping(source = "periodType.name", target = "periodTypeName")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<DataPeriodDto> toDtoList(List<DataPeriod> entityList);

    @Override
    @Mapping(source = "organizationUnitId", target = "organizationUnit.id")
    @Mapping(source = "organizationUnitName", target = "organizationUnit.name")
    @Mapping(source = "periodTypeId", target = "periodType.id")
    @Mapping(source = "periodTypeName", target = "periodType.name")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<DataPeriod> toEntityList(List<DataPeriodDto> dtoList);

    @Override
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    @Mapping(source = "periodType.id", target = "periodTypeId")
    @Mapping(source = "periodType.name", target = "periodTypeName")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DataPeriodDto toDto(DataPeriod entity);


}
