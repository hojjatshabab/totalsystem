package fava.betaja.erp.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
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

}
