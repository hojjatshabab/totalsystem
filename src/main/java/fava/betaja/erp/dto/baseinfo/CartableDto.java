package fava.betaja.erp.dto.baseinfo;

import fava.betaja.erp.enums.baseinfo.CartableState;
import fava.betaja.erp.enums.baseinfo.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartableDto {

    private UUID id;
    private String title;
    private UUID documentId;
    private String documentNumber;
    private CartableState state;
    private Long senderId;
    private String senderName;
    private String senderLastname;
    private Long recipientId;
    private String recipientName;
    private String recipientLastname;
    private String description;
    private LocalDate sendDate;
    private UUID flowRuleDomainId;
    private String flowRuleDomainEntityName;
    private String flowRuleName;
    private UUID currentStepId;
    private UUID nextStepId;
    private Priority priority = Priority.NORMAL;
    private Boolean read = false;
    private Date dueDate;
    private List<FileStorageDto> attachments;

}

