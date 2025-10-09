package fava.betaja.erp.mapper.baseinfo;

import fava.betaja.erp.dto.baseinfo.CartableHistoryDto;
import fava.betaja.erp.entities.baseinfo.CartableHistory;
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
public interface CartableHistoryDtoMapper extends BaseMapper<CartableHistoryDto, CartableHistory> {

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "cartable.id", source = "cartableId")
    CartableHistory toEntity(CartableHistoryDto dto);

    List<CartableHistory> toEntityList(List<CartableHistoryDto> dtos);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFirstname", source = "user.firstname")
    @Mapping(target = "userLastname", source = "user.lastname")
    @Mapping(target = "cartableId", source = "cartable.id")
    @Mapping(target = "cartableTitle", source = "cartable.title")
    CartableHistoryDto toDto(CartableHistory entity);

    List<CartableHistoryDto> toDtoList(List<CartableHistory> entities);


}
