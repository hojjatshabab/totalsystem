package fava.betaja.erp.mapper.common;


import fava.betaja.erp.dto.common.CommonTypeDto;
import fava.betaja.erp.entities.common.CommonType;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        ,componentModel = "spring")
public interface CommonTypeDtoMapper extends BaseMapper<CommonTypeDto, CommonType> {

    @Override
    CommonType toEntity(CommonTypeDto dto);

    @Override
    List<CommonTypeDto> toDtoList(List<CommonType> entityList);

    @Override
    List<CommonType> toEntityList(List<CommonTypeDto> dtoList);

    @Override
    CommonTypeDto toDto(CommonType entity);
}
