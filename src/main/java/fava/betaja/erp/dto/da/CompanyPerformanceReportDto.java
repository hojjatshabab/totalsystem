package fava.betaja.erp.dto.da;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyPerformanceReportDto {
    private Long companyId;
    private String companyName;

    private Integer totalProjects;         // تعداد پروژه‌ها
    private Integer delayedProjects;       // پروژه‌هایی که انحراف منفی دارند
    private BigDecimal avgProgressPlan;    // میانگین پیشرفت برنامه‌ای
    private BigDecimal avgProgressActual;  // میانگین پیشرفت واقعی
    private BigDecimal avgDeviation;       // میانگین انحراف
}

