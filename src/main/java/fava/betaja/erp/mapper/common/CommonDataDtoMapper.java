package fava.betaja.erp.mapper.common;


import fava.betaja.erp.dto.common.CommonDataDto;
import fava.betaja.erp.entities.common.CommonData;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        ,componentModel = "spring")
public interface CommonDataDtoMapper  extends BaseMapper<CommonDataDto, CommonData> {

    @Override
    @Mapping(source = "commonType.typeName",target = "typeName")
    @Mapping(source = "commonType.id",target = "typeId")
    List<CommonDataDto> toDtoList(List<CommonData> entityList);

    @Override
    List<CommonData> toEntityList(List<CommonDataDto> dtoList);

    @Override
    @Mapping(source = "commonType.typeName",target = "typeName")
    @Mapping(source = "commonType.id",target = "typeId")
    CommonDataDto toDto(CommonData entity);

    @Override
    CommonData toEntity(CommonDataDto dto);
}
