package fava.betaja.erp.dto.da;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationalPerformanceInfoDto {

    private UUID id;
    private Long actionCount;
    private Long commonDataActionId;
    private String commonDataActionName;
    private UUID organizationalPerformanceId;
    private String organizationalPerformanceTitle;


}



