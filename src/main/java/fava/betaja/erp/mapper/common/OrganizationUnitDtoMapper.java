package fava.betaja.erp.mapper.common;


import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.mapper.BaseMapper;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        , componentModel = "spring")
public interface OrganizationUnitDtoMapper extends BaseMapper<OrganizationUnitDto, OrganizationUnit> {

    @Override
    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(source = "commonBaseDataOrgTypeId", target = "commonBaseDataOrgType.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizationUnit toEntity(OrganizationUnitDto dto);

    @Override
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    @Mapping(source = "commonBaseDataOrgType.id", target = "commonBaseDataOrgTypeId")
    @Mapping(source = "commonBaseDataOrgType.value", target = "commonBaseDataOrgTypeValue")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<OrganizationUnitDto> toDtoList(List<OrganizationUnit> entityList);

    @Override
    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(source = "commonBaseDataOrgTypeId", target = "commonBaseDataOrgType.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<OrganizationUnit> toEntityList(List<OrganizationUnitDto> dtoList);

    @Override
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    @Mapping(source = "commonBaseDataOrgType.id", target = "commonBaseDataOrgTypeId")
    @Mapping(source = "commonBaseDataOrgType.value", target = "commonBaseDataOrgTypeValue")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizationUnitDto toDto(OrganizationUnit entity);

    @AfterMapping
    default void convertToNull(@MappingTarget OrganizationUnit organizationUnit) {

        if (Objects.isNull(organizationUnit.getParent()) ||
                Objects.isNull(organizationUnit.getParent().getId())) {
            organizationUnit.setParent(null);
        }
        if (Objects.isNull(organizationUnit.getCommonBaseDataOrgType()) ||
                Objects.isNull(organizationUnit.getCommonBaseDataOrgType().getId())) {
            organizationUnit.setCommonBaseDataOrgType(null);
        }
    }
}