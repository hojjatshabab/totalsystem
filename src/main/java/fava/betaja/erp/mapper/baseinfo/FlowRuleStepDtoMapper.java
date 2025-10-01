package fava.betaja.erp.mapper.baseinfo;

import fava.betaja.erp.dto.baseinfo.FlowRuleStepDto;
import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FlowRuleStepDtoMapper extends BaseMapper<FlowRuleStepDto, FlowRuleStep> {

    @Mapping(target = "flowRule.id", source = "flowRuleId")
    @Mapping(target = "role.id", source = "roleId")
    @Mapping(target = "organizationUnit.id", source = "organizationUnitId")
    FlowRuleStep toEntity(FlowRuleStepDto dto);

    List<FlowRuleStep> toEntityList(List<FlowRuleStepDto> dtos);

    @Mapping(target = "flowRuleId", source = "flowRule.id")
    @Mapping(target = "flowRuleName", source = "flowRule.name")
    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(source = "organizationUnit.id", target = "organizationUnitId")
    @Mapping(source = "organizationUnit.name", target = "organizationUnitName")
    FlowRuleStepDto toDto(FlowRuleStep entity);

    List<FlowRuleStepDto> toDtoList(List<FlowRuleStep> entities);

    @AfterMapping
    default void convertToNull(@MappingTarget FlowRuleStep entity) {
        if (Objects.isNull(entity.getOrganizationUnit())) {
            entity.setOrganizationUnit(null);
        } else {
            if (Objects.isNull(entity.getOrganizationUnit().getId()))
                entity.setOrganizationUnit(null);
        }
    }
}
