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
public class BlockDto {

    private UUID id;
    private String name;
    private Integer blockCount;
    private Integer floorCount;
    private Integer unitCount;
    private BigDecimal totalArea;
    private LocalDate startDate;
    private LocalDate deliveryDate;
    private UUID projectId;
    private String projectName;
    private UUID planId;
    private String planName;
    private Long organizationUnitId;
    private String organizationUnitName;
}

