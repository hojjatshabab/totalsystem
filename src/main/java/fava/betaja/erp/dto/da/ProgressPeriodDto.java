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
public class ProgressPeriodDto {

    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private String title;
    private BigDecimal valuePlanned;
    private UUID referenceId;
    private String referenceType;
    private String referenceName;
    private Long organizationId;
    private String organizationName;
    private UUID periodRangeId;
    private String periodRangeName;

}
