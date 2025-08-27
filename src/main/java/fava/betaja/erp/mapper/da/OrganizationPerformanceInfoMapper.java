package fava.betaja.erp.mapper.da;

import fava.betaja.erp.entities.da.OrganizationalPerformanceInfo;
import fava.betaja.erp.dto.da.OrganizationalPerformanceInfoDto;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        , componentModel = "spring")
public interface OrganizationPerformanceInfoMapper extends BaseMapper<OrganizationalPerformanceInfoDto, OrganizationalPerformanceInfo> {

    @Override
    @Mapping(source = "commonDataActionId",target = "commonDataAction.id")
    @Mapping(source = "organizationalPerformanceId",target = "organizationalPerformance.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizationalPerformanceInfo toEntity(OrganizationalPerformanceInfoDto dto);

    @Override
    @Mapping(source = "commonDataAction.id",target = "commonDataActionId")
    @Mapping(source = "commonDataAction.value",target = "commonDataActionName")
    @Mapping(source = "organizationalPerformance.id",target = "organizationalPerformanceId")
    @Mapping(source = "organizationalPerformance.title",target = "organizationalPerformanceTitle")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<OrganizationalPerformanceInfoDto> toDtoList(List<OrganizationalPerformanceInfo> entityList);

    @Override
    @Mapping(source = "commonDataActionId",target = "commonDataAction.id")
    @Mapping(source = "organizationalPerformanceId",target = "organizationalPerformance.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<OrganizationalPerformanceInfo> toEntityList(List<OrganizationalPerformanceInfoDto> dtoList);

    @Override
    @Mapping(source = "commonDataAction.id",target = "commonDataActionId")
    @Mapping(source = "commonDataAction.value",target = "commonDataActionName")
    @Mapping(source = "organizationalPerformance.id",target = "organizationalPerformanceId")
    @Mapping(source = "organizationalPerformance.title",target = "organizationalPerformanceTitle")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizationalPerformanceInfoDto toDto(OrganizationalPerformanceInfo entity);



}
