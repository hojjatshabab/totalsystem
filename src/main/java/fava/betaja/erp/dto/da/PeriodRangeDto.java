package fava.betaja.erp.dto.da;

import fava.betaja.erp.enums.da.RangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodRangeDto {

    private UUID id;
    private String name;
    private Integer orderNo;
    private RangeType rangeType;
    private String key;
    private Integer durationDays;
}
