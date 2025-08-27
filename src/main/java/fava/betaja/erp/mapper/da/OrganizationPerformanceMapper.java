package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.OrganizationalPerformanceDto;
import fava.betaja.erp.entities.common.CommonData;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.da.OrganizationalPerformance;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        , componentModel = "spring")
public interface OrganizationPerformanceMapper extends BaseMapper<OrganizationalPerformanceDto, OrganizationalPerformance> {

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "informationTypeId", target = "informationType.id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "organizationUnitId", target = "organizationUnit.id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizationalPerformance toEntity(OrganizationalPerformanceDto dto);

    @Override
    @Mapping(source = "informationType.id", target = "informationTypeId")
    @Mapping(source = "informationType.value", target = "informationTypeName")
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    List<OrganizationalPerformanceDto> toDtoList(List<OrganizationalPerformance> entityList);


    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "informationType.id", target = "informationTypeId")
    @Mapping(source = "informationType.value", target = "informationTypeName")
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    List<OrganizationalPerformance> toEntityList(List<OrganizationalPerformanceDto> dtoList);

    @Override
    @Mapping(source = "informationType.value", target = "informationTypeName")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    OrganizationalPerformanceDto toDto(OrganizationalPerformance entity);


    @Named("mapInformationTypeIdToEntity")
    default CommonData mapInformationTypeIdToEntity(Long informationTypeId) {
        if (informationTypeId == null) {
            return null;
        }
        CommonData infoType = new CommonData();
        infoType.setId(informationTypeId);
        return infoType;
    }

    @Named("mapOrganizationUnitIdToEntity")
    default OrganizationUnit mapOrganizationUnitIdToEntity(Long organizationUnitId) {
        if (organizationUnitId == null) {
            return null;
        }
        OrganizationUnit organizationUnit = new OrganizationUnit();
        organizationUnit.setId(organizationUnitId);
        return organizationUnit;
    }

}
