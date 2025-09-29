package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.BlockValueDto;
import fava.betaja.erp.entities.da.BlockValue;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BlockValueDtoMapper extends BaseMapper<BlockValueDto, BlockValue> {

    @Override
    @Mapping(target = "blockPeriod.id", source = "blockPeriodId")
    BlockValue toEntity(BlockValueDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<BlockValue> toEntityList(List<BlockValueDto> dtoList);

    @Override
    @Mapping(source = "blockPeriod.id", target = "blockPeriodId")
    @Mapping(source = "blockPeriod.title", target = "blockPeriodTitle")
    BlockValueDto toDto(BlockValue entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<BlockValueDto> toDtoList(List<BlockValue> entityList);


}
