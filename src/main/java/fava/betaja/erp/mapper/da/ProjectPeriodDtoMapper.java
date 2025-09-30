package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.ProjectPeriodDto;
import fava.betaja.erp.entities.da.ProjectPeriod;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProjectPeriodDtoMapper extends BaseMapper<ProjectPeriodDto, ProjectPeriod> {

    @Override
    @Mapping(target = "periodRange.id", source = "periodRangeId")
    @Mapping(target = "project.id", source = "projectId")
    ProjectPeriod toEntity(ProjectPeriodDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<ProjectPeriod> toEntityList(List<ProjectPeriodDto> dtoList);

    @Override
    @Mapping(source = "periodRange.id", target = "periodRangeId")
    @Mapping(source = "periodRange.name", target = "periodRangeName")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    ProjectPeriodDto toDto(ProjectPeriod entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<ProjectPeriodDto> toDtoList(List<ProjectPeriod> entityList);


}
