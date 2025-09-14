package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.PeriodRangeDto;
import fava.betaja.erp.entities.da.PeriodRange;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        , componentModel = "spring")
public interface PeriodRangeDtoMapper extends BaseMapper<PeriodRangeDto, PeriodRange> {

    @Override
    PeriodRange toEntity(PeriodRangeDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<PeriodRange> toEntityList(List<PeriodRangeDto> dtoList);

    @Override
    PeriodRangeDto toDto(PeriodRange entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<PeriodRangeDto> toDtoList(List<PeriodRange> entityList);

}
