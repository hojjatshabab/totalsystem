package fava.betaja.erp.dto.baseinfo;

import fava.betaja.erp.enums.baseinfo.ActionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartableHistoryDto {

    private CartableDto cartable;
    private Long userId;
    private String username;
    private String userFirstname;
    private String userLastname;
    private ActionTypeEnum actionType;
    private String comment;
    private Date actionDate = new Date();
}

