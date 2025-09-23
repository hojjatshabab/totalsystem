package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.ProjectDto;
import fava.betaja.erp.entities.da.Project;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProjectDtoMapper extends BaseMapper<ProjectDto, Project> {

    @Override
    @Mapping(target = "plan.organizationUnit.id", source = "organizationUnitId")
    @Mapping(target = "plan.id", source = "planId")
    Project toEntity(ProjectDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Project> toEntityList(List<ProjectDto> dtoList);

    @Override
    @Mapping(source = "plan.id", target = "planId")
    @Mapping(source = "plan.name", target = "planName")
    @Mapping(source = "plan.organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "plan.organizationUnit.name", target = "organizationUnitName")
    ProjectDto toDto(Project entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<ProjectDto> toDtoList(List<Project> entityList);
}
