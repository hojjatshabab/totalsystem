package fava.betaja.erp.dto.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonBaseTypeDto {

    private Long id;
    private String className;
    private Boolean active = true;
    private String title;
    private String description;

}
