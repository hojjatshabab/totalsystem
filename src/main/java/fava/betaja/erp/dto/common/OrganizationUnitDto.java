package fava.betaja.erp.dto.common;

import fava.betaja.erp.entities.common.OrganizationUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrganizationUnitDto {


    private Long id;

    private String name;


    private Long parentId;


    private Long orgTypeId;



}
