package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.BlockPeriodDto;
import fava.betaja.erp.entities.da.BlockPeriod;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BlockPeriodDtoMapper extends BaseMapper<BlockPeriodDto, BlockPeriod> {

    @Override
    @Mapping(target = "periodRange.id", source = "periodRangeId")
    @Mapping(target = "block.id", source = "blockId")
    BlockPeriod toEntity(BlockPeriodDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<BlockPeriod> toEntityList(List<BlockPeriodDto> dtoList);

    @Override
    @Mapping(source = "periodRange.id", target = "periodRangeId")
    @Mapping(source = "periodRange.name", target = "periodRangeName")
    @Mapping(source = "block.id", target = "blockId")
    @Mapping(source = "block.name", target = "blockName")
    BlockPeriodDto toDto(BlockPeriod entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<BlockPeriodDto> toDtoList(List<BlockPeriod> entityList);


}
