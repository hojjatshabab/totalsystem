package fava.betaja.erp.mapper.common;

import fava.betaja.erp.dto.common.CommonBaseDataDto;
import fava.betaja.erp.entities.common.CommonBaseData;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CommonBaseDataDtoMapper extends BaseMapper<CommonBaseDataDto, CommonBaseData> {

    @Mapping(target = "commonBaseType.id", source = "commonBaseTypeId")
    CommonBaseData toEntity(CommonBaseDataDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<CommonBaseData> toEntityList(List<CommonBaseDataDto> dtoList);

    @Override
    @Mapping(source = "commonBaseType.id", target = "commonBaseTypeId")
    @Mapping(source = "commonBaseType.title", target = "commonBaseTypeTitle")
    CommonBaseDataDto toDto(CommonBaseData entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<CommonBaseDataDto> toDtoList(List<CommonBaseData> entityList);
}
