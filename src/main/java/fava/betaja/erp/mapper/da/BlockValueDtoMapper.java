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
    @Mapping(target = "projectPeriod.id", source = "projectPeriodId")
    @Mapping(target = "block.id", source = "blockId")
    BlockValue toEntity(BlockValueDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<BlockValue> toEntityList(List<BlockValueDto> dtoList);

    @Override
    @Mapping(source = "projectPeriod.id", target = "projectPeriodId")
    @Mapping(source = "projectPeriod.title", target = "projectPeriodTitle")
    @Mapping(source = "block.id", target = "blockId")
    @Mapping(source = "block.name", target = "blockName")
    @Mapping(source = "projectPeriod.project.id", target = "projectId")
    @Mapping(source = "projectPeriod.project.name", target = "projectName")
    @Mapping(source = "projectPeriod.project.plan.id", target = "planId")
    @Mapping(source = "projectPeriod.project.plan.name", target = "planName")
    BlockValueDto toDto(BlockValue entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<BlockValueDto> toDtoList(List<BlockValue> entityList);


}
