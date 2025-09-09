package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.PeriodTypeDto;
import fava.betaja.erp.entities.da.PeriodType;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        , componentModel = "spring")
public interface PeriodTypeDtoMapper extends BaseMapper<PeriodTypeDto, PeriodType> {

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PeriodType toEntity(PeriodTypeDto dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<PeriodTypeDto> toDtoList(List<PeriodType> entityList);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<PeriodType> toEntityList(List<PeriodTypeDto> dtoList);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PeriodTypeDto toDto(PeriodType entity);


}
