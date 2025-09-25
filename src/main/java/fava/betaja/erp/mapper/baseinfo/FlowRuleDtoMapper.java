package fava.betaja.erp.mapper.baseinfo;

import fava.betaja.erp.dto.baseinfo.FlowRuleDto;
import fava.betaja.erp.entities.baseinfo.FlowRule;
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
public interface FlowRuleDtoMapper extends BaseMapper<FlowRuleDto, FlowRule> {

    FlowRule toEntity(FlowRuleDto dto);

    List<FlowRule> toEntityList(List<FlowRuleDto> dtos);

    FlowRuleDto toDto(FlowRule entity);

    List<FlowRuleDto> toDtoList(List<FlowRule> entities);

}
