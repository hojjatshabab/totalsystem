package fava.betaja.erp.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonDataDto {

    private Long id;
    private String value;
    private String key;
    private Long typeId;
    private String typeName;
}
