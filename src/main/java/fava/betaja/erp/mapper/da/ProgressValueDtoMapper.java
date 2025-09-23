package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.ProgressValueDto;
import fava.betaja.erp.entities.da.ProgressValue;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProgressValueDtoMapper extends BaseMapper<ProgressValueDto, ProgressValue> {

    @Mapping(target = "progressPeriod.id", source = "progressPeriodId")
    @Mapping(target = "periodRange.id", source = "periodRangeId")
    @Mapping(target = "progressPeriod.referenceId", source = "referenceId")
    @Mapping(target = "progressPeriod.referenceType", source = "referenceType")
    ProgressValue toEntity(ProgressValueDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<ProgressValue> toEntityList(List<ProgressValueDto> dtoList);

    @Override
    @Mapping(source = "progressPeriod.id", target = "progressPeriodId")
    @Mapping(source = "progressPeriod.title", target = "progressPeriodTitle")
    @Mapping(source = "progressPeriod.referenceId", target = "referenceId")
    @Mapping(source = "progressPeriod.referenceType", target = "referenceType")
    @Mapping(source = "periodRange.id", target = "periodRangeId")
    @Mapping(source = "periodRange.name", target = "periodRangeName")
    ProgressValueDto toDto(ProgressValue entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<ProgressValueDto> toDtoList(List<ProgressValue> entityList);

}
