package fava.betaja.erp.dto.da;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrganizationalPerformanceDto {

    private UUID    id;
    private Date informationDate;

    private String description;

    private Long informationTypeId;
    private String informationTypeName;

    private Long organizationUnitId;

    private String organizationUnitName;





}
