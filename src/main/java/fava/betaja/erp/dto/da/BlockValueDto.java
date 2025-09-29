package fava.betaja.erp.dto.da;

import fava.betaja.erp.entities.AbstractAuditingEntity;
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
public class BlockValueDto extends AbstractAuditingEntity {

    private UUID id;
    private String name;
    private Integer blockCount;
    private Integer floorCount;
    private Integer unitCount;
    private BigDecimal totalArea;
    private LocalDate startDate;
    private LocalDate deliveryDate;
    private BigDecimal progressActual;
    private BigDecimal progressPlan;
    private BigDecimal deviation;
    private BigDecimal progressActualInPeriod;
    private BigDecimal progressPlanInPeriod;
    private String activitySummary;
    private String issueSummary;
    private UUID blockPeriodId;
    private String blockPeriodTitle;
}
