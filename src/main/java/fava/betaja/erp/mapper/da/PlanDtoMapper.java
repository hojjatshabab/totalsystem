package fava.betaja.erp.mapper.da;

import fava.betaja.erp.dto.da.PlanDto;
import fava.betaja.erp.entities.da.Plan;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PlanDtoMapper extends BaseMapper<PlanDto, Plan> {

    @Mapping(target = "organizationUnit.id", source = "organizationUnitId")
    Plan toEntity(PlanDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<Plan> toEntityList(List<PlanDto> dtoList);

    @Override
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    PlanDto toDto(Plan entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<PlanDto> toDtoList(List<Plan> entityList);
}
