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
public class DataPeriodDto {

    private UUID id;
    private Date startDate;
    private Date endDate;
    private Boolean isActive;
    private Long organizationUnitId;
    private String organizationUnitName;
    private UUID periodTypeId;
    private String periodTypeName;
}
