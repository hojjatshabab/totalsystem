package fava.betaja.erp.dto.da;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPeriodDto {

    private UUID id;
    private String title;
    private String year;
    private Boolean isActive;
    private UUID periodRangeId;
    private String periodRangeName;
    private UUID projectId;
    private String projectName;

}
