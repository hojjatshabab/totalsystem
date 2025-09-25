package fava.betaja.erp.mapper.baseinfo;

import fava.betaja.erp.dto.baseinfo.FlowRuleDomainDto;
import fava.betaja.erp.entities.baseinfo.FlowRuleDomain;
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
public interface FlowRuleDomainDtoMapper extends BaseMapper<FlowRuleDomainDto, FlowRuleDomain> {

    @Mapping(target = "flowRule.id", source = "flowRuleId")
    FlowRuleDomain toEntity(FlowRuleDomainDto dto);

    List<FlowRuleDomain> toEntityList(List<FlowRuleDomainDto> dtos);

    @Mapping(target = "flowRuleId", source = "flowRule.id")
    @Mapping(target = "flowRuleName", source = "flowRule.name")
    FlowRuleDomainDto toDto(FlowRuleDomain entity);

    List<FlowRuleDomainDto> toDtoList(List<FlowRuleDomain> entities);

}
