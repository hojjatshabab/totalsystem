package fava.betaja.erp.mapper.da;

import fava.betaja.erp.entities.da.OrganizationalPerformanceInfo;
import fava.betaja.erp.dto.da.OrganizationalPerformanceInfoDto;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OrganizationPerformanceInfoMapper extends BaseMapper<OrganizationalPerformanceInfoDto, OrganizationalPerformanceInfo> {

    @Override

    @Mapping(source = "commonDataActionId",target = "commonDataAction.id")
    OrganizationalPerformanceInfo toEntity(OrganizationalPerformanceInfoDto dto);


    @Override
    @Mapping(source = "commonDataAction.id",target = "commonDataActionId")
    @Mapping(source = "commonDataAction.name",target = "commonDataActionName")
    List<OrganizationalPerformanceInfoDto> toDtoList(List<OrganizationalPerformanceInfo> entityList);

    @Override
    @Mapping(source = "commonDataActionId",target = "commonDataAction.id")
    List<OrganizationalPerformanceInfo> toEntityList(List<OrganizationalPerformanceInfoDto> dtoList);

    @Override
    @Mapping(source = "commonDataAction.id",target = "commonDataActionId")
    @Mapping(source = "commonDataAction.value",target = "commonDataActionName")
    OrganizationalPerformanceInfoDto toDto(OrganizationalPerformanceInfo entity);



}
