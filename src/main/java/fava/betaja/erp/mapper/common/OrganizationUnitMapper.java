package fava.betaja.erp.mapper.common;


import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        ,componentModel = "spring")
public interface OrganizationUnitMapper extends BaseMapper<OrganizationUnitDto, OrganizationUnit> {

    @Override
    public OrganizationUnit toEntity(OrganizationUnitDto dto) ;

    @Override
    public OrganizationUnitDto toDto(OrganizationUnit entity) ;

    @Override
    public List<OrganizationUnit> toEntityList(List<OrganizationUnitDto> dtoList) ;

    @Override
    public List<OrganizationUnitDto> toDtoList(List<OrganizationUnit> entityList) ;


}
