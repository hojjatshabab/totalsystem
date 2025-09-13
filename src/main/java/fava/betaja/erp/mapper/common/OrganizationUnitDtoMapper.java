package fava.betaja.erp.mapper.common;

import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.CommonData;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrganizationUnitDtoMapper extends BaseMapper<OrganizationUnitDto, OrganizationUnit> {

    @Mapping(target = "parent", source = "parentId", qualifiedByName = "mapParent")
    @Mapping(target = "commonBaseDataOrgType", source = "commonBaseDataOrgTypeId", qualifiedByName = "mapCommonData")
    OrganizationUnit toEntity(OrganizationUnitDto dto);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<OrganizationUnit> toEntityList(List<OrganizationUnitDto> dtoList);

    @Override
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    @Mapping(source = "commonBaseDataOrgType.id", target = "commonBaseDataOrgTypeId")
    @Mapping(source = "commonBaseDataOrgType.value", target = "commonBaseDataOrgTypeValue")
    OrganizationUnitDto toDto(OrganizationUnit entity);

    @Override
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    List<OrganizationUnitDto> toDtoList(List<OrganizationUnit> entityList);

    @Named("mapParent")
    default OrganizationUnit mapParent(Long parentId) {
        return parentId != null ? OrganizationUnit.builder().id(parentId).build() : null;
    }

    @Named("mapCommonData")
    default CommonData mapCommonData(Long commonBaseDataOrgTypeId) {
        return commonBaseDataOrgTypeId != null ? CommonData.builder().id(commonBaseDataOrgTypeId).build() : null;
    }
}
