package fava.betaja.erp.mapper.baseinfo;

import fava.betaja.erp.dto.baseinfo.FlowRuleStepDto;
import fava.betaja.erp.entities.baseinfo.FlowRuleStep;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

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

}
