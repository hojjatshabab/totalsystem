package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.da.AttributePeriodProgressDto;
import fava.betaja.erp.entities.da.AttributePeriod;
import fava.betaja.erp.entities.da.AttributeValue;
import fava.betaja.erp.entities.da.PeriodRange;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.repository.da.AttributePeriodRepository;
import fava.betaja.erp.repository.da.AttributeValueRepository;
import fava.betaja.erp.service.da.AttributePeriodReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AttributePeriodReportServiceImpl implements AttributePeriodReportService {

    private final AttributePeriodRepository attributePeriodRepository;
    private final AttributeValueRepository attributeValueRepository;

    @Override
    public AttributePeriodProgressDto getProgress(UUID attributePeriodId) {
        AttributePeriod period = attributePeriodRepository.findById(attributePeriodId)
                .orElseThrow(() -> new ServiceException("AttributePeriod not found"));

        BigDecimal plannedValue = period.getValuePlanned() != null ? period.getValuePlanned() : BigDecimal.ZERO;

        BigDecimal totalValue = attributeValueRepository.sumValueByAttributePeriodId(attributePeriodId);
        if (totalValue == null) totalValue = BigDecimal.ZERO;

        // --- محاسبه روزها (شامل ابتدا و انتهای بازه)
        long durationDays = period.getPeriodRange().getDurationDays();
        long elapsedDays = attributeValueRepository.findByAttributePeriodId(period.getId()).stream()
                .map(AttributeValue::getPeriodRange)         // گرفتن periodRange
                .filter(Objects::nonNull)                   // حذف null
                .map(PeriodRange::getDurationDays)          // گرفتن durationDays
                .filter(Objects::nonNull)                   // حذف null
                .mapToLong(Integer::longValue)              // تبدیل به long
                .sum();

        if (elapsedDays < 0) elapsedDays = 0;
        if (durationDays <= 0) durationDays = 1; // جلوگیری از تقسیم بر صفر
        if (elapsedDays > durationDays) elapsedDays = durationDays;

        // ratio = elapsedDays / durationDays
        BigDecimal ratio = BigDecimal.valueOf(elapsedDays)
                .divide(BigDecimal.valueOf(durationDays), 10, RoundingMode.HALF_UP);

        // مقدار مورد انتظار تا الان
        BigDecimal expectedValueUntilNow = plannedValue.multiply(ratio)
                .setScale(2, RoundingMode.HALF_UP);

        // درصد مورد انتظار تا الان (0-100)
        BigDecimal expectedPercentUntilNow = ratio.multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);

        // درصد واقعی تکمیل‌شده
        double percentCompleted = plannedValue.compareTo(BigDecimal.ZERO) > 0
                ? totalValue.divide(plannedValue, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue()
                : 0.0;

        BigDecimal remainingValue = plannedValue.subtract(totalValue).setScale(2, RoundingMode.HALF_UP);

        String status;
        if (totalValue.compareTo(expectedValueUntilNow) > 0) status = "AHEAD";
        else if (totalValue.compareTo(expectedValueUntilNow) < 0) status = "BEHIND";
        else status = "ON_TRACK";

        return AttributePeriodProgressDto.builder()
                .attributePeriodId(attributePeriodId)
                .attributePeriodTitle(period.getTitle())
                .plannedValue(plannedValue.setScale(2, RoundingMode.HALF_UP))
                .totalValue(totalValue.setScale(2, RoundingMode.HALF_UP))
                .percentCompleted(percentCompleted)
                .expectedValueUntilNow(expectedValueUntilNow)
                .expectedPercentUntilNow(expectedPercentUntilNow.doubleValue())
                .remainingValue(remainingValue)
                .status(status)
                .build();
    }
}

