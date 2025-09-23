package fava.betaja.erp.dto.da;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressValueDto {

    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal value;
    private String activitySummary;
    private String issueSummary;
    private UUID periodRangeId;
    private String periodRangeName;
    private UUID progressPeriodId;
    private String progressPeriodTitle;
    private UUID referenceId;
    private String referenceType;
    private Long organizationId;
    private String organizationName;
    private BigDecimal valuePlanned;

}
