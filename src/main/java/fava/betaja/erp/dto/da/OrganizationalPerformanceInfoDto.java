package fava.betaja.erp.dto.da;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrganizationalPerformanceInfoDto {

        private UUID id;
        private Long actionCount;
        private Long commonDataActionId;
        private Long commonDataActionName;
        private Long fileInfoId;

}



