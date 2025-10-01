package fava.betaja.erp.mapper.baseinfo;

import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {FileStorageDtoMapper.class}
)
public interface CartableDtoMapper extends BaseMapper<CartableDto, Cartable> {

    @Mapping(target = "sender.id", source = "senderId")
    @Mapping(target = "recipient.id", source = "recipientId")
    @Mapping(target = "flowRuleDomain.id", source = "flowRuleDomainId")
    @Mapping(target = "currentStep.id", source = "currentStepId")
    Cartable toEntity(CartableDto dto);

    List<Cartable> toEntityList(List<CartableDto> dtos);

    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderName", source = "sender.firstname")
    @Mapping(target = "senderLastname", source = "sender.lastname")
    @Mapping(target = "recipientId", source = "recipient.id")
    @Mapping(target = "recipientName", source = "recipient.firstname")
    @Mapping(target = "recipientLastname", source = "recipient.lastname")
    @Mapping(target = "flowRuleDomainId", source = "flowRuleDomain.id")
    @Mapping(target = "flowRuleDomainEntityName", source = "flowRuleDomain.entityName")
    @Mapping(target = "flowRuleId", source = "flowRuleDomain.flowRule.id")
    @Mapping(target = "flowRuleName", source = "flowRuleDomain.flowRule.name")
    @Mapping(target = "currentStepId", source = "currentStep.id")
    CartableDto toDto(Cartable entity);

    List<CartableDto> toDtoList(List<Cartable> entities);

}
