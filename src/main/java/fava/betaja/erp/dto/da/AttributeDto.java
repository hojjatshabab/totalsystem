package fava.betaja.erp.dto.da;

import fava.betaja.erp.enums.AttributeDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDto {

    private UUID id;
    private String name;
    private AttributeDataType dataType;
    private Long organizationUnitId;
    private String organizationUnitName;
}
