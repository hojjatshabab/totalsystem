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
public class BlockDto {

    private UUID id;
    private String name;
    private String key;
    private UUID projectId;
    private String projectName;
}
/*    public String getReferenceName(UUID referenceId, String referenceType) {
        switch(referenceType) {
            case "PROJECT":
                return projectRepository.findById(referenceId)
                        .map(Project::getName)
                        .orElse("Unknown Project");
            case "BLOCK":
                return blockRepository.findById(referenceId)
                        .map(Block::getName)
                        .orElse("Unknown Block");
            case "ATTRIBUTE":
                return attributeRepository.findById(referenceId)
                        .map(Attribute::getName)
                        .orElse("Unknown Attribute");
            default:
                return "Unknown";
        }
    }*/
