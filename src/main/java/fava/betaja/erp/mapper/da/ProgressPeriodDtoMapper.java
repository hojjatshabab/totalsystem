package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.ProgressPeriodDto;
import fava.betaja.erp.entities.da.ProgressPeriod;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProgressPeriodDtoMapper extends BaseMapper<ProgressPeriodDto, ProgressPeriod> {

    @Override
    @Mapping(target = "periodRange.id", source = "periodRangeId")
    ProgressPeriod toEntity(ProgressPeriodDto dto);


    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<ProgressPeriod> toEntityList(List<ProgressPeriodDto> dtoList);

    @Override
    @Mapping(source = "periodRange.id", target = "periodRangeId")
    @Mapping(source = "periodRange.name", target = "periodRangeName")
    ProgressPeriodDto toDto(ProgressPeriod entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<ProgressPeriodDto> toDtoList(List<ProgressPeriod> entityList);

}
