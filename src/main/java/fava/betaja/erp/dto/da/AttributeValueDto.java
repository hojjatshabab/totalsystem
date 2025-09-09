package fava.betaja.erp.dto.da;

import fava.betaja.erp.enums.ValueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueDto {

    private UUID id;
    private Integer value;
    private ValueType valueType;
    private Long organizationUnitId;
    private String organizationUnitName;
    private UUID attributeId;
    private String attributeName;
    private UUID dataPeriodId;
}
