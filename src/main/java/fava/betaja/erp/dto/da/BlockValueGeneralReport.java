package fava.betaja.erp.dto.da;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockValueGeneralReport {
    String companyName;
    List<PlanReport> planReports;
}
