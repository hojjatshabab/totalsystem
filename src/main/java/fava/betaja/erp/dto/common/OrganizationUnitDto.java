package fava.betaja.erp.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUnitDto {

    private Long id;
    private String name;
    private String address;
    private String tellNumber;
    private String code;
    private Boolean active;
    private String codePath;
    private Long commonBaseDataOrgTypeId;
    private String commonBaseDataOrgTypeValue;
    private Long parentId;
    private String parentName;
    private List<OrganizationUnitDto> children;

}
