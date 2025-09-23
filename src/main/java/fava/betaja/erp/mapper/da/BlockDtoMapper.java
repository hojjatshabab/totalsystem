package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.BlockDto;
import fava.betaja.erp.entities.da.Block;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BlockDtoMapper extends BaseMapper<BlockDto, Block> {

    @Mapping(target = "project.plan.organizationUnit.id", source = "organizationUnitId")
    @Mapping(target = "project.plan.id", source = "planId")
    @Mapping(target = "project.id", source = "projectId")
    Block toEntity(BlockDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Block> toEntityList(List<BlockDto> dtoList);

    @Override
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "project.plan.id", target = "planId")
    @Mapping(source = "project.plan.name", target = "planName")
    @Mapping(source = "project.plan.organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "project.plan.organizationUnit.name", target = "organizationUnitName")
    BlockDto toDto(Block entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<BlockDto> toDtoList(List<Block> entityList);
}
