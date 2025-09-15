package fava.betaja.erp.dto.da;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributePeriodProgressDto {

    private UUID attributePeriodId;
    private String attributePeriodTitle;
    private BigDecimal plannedValue;
    private BigDecimal totalValue;
    private Double percentCompleted;
    private BigDecimal expectedValueUntilNow;
    private Double expectedPercentUntilNow;
    private BigDecimal remainingValue;
    private String status; // "AHEAD" , "BEHIND" , "ON_TRACK"
}
