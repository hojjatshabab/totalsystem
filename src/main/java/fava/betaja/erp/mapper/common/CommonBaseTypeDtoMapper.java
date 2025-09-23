package fava.betaja.erp.mapper.common;


import fava.betaja.erp.dto.common.CommonBaseTypeDto;
import fava.betaja.erp.entities.common.CommonBaseType;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CommonBaseTypeDtoMapper extends BaseMapper<CommonBaseTypeDto, CommonBaseType> {

    @Override
    CommonBaseType toEntity(CommonBaseTypeDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<CommonBaseType> toEntityList(List<CommonBaseTypeDto> dtoList);

    @Override
    CommonBaseTypeDto toDto(CommonBaseType entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<CommonBaseTypeDto> toDtoList(List<CommonBaseType> entityList);
}
