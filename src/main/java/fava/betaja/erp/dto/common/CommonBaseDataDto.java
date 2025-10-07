package fava.betaja.erp.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonBaseDataDto {

    private Long id;
    private Boolean active = true;
    private String value;
    private String key;
    private Integer orderNo;
    private Long commonBaseTypeId;
    private String commonBaseTypeTitle;
}
