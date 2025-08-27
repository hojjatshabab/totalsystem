package fava.betaja.erp.dto.da;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationalPerformanceDto {

    private UUID id;
    private Date informationDate;
    private String description;
    private String title;
    private Long informationTypeId;
    private String informationTypeName;
    private Long organizationUnitId;
    private String organizationUnitName;

}
