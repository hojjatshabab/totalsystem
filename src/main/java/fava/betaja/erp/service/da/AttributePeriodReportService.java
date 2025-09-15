package fava.betaja.erp.service.da;

import fava.betaja.erp.dto.da.AttributePeriodProgressDto;

import java.util.UUID;

public interface AttributePeriodReportService {
    AttributePeriodProgressDto getProgress(UUID attributePeriodId);
}
