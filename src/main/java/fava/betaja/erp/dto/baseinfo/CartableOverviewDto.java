package fava.betaja.erp.dto.baseinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartableOverviewDto {
    private int pendingCount;           // در انتظار اقدام
    private int returnedCount;          // برگشتی‌ها
    private int inProgressCount;        // در حال بررسی
    private int approvedCount;          // تایید شده‌ها
    private int totalCount;             // مجموع کل

    private Instant lastActionDate; // آخرین زمان فعالیت کاربر
    private String lastActionTitle;       // عنوان آخرین گردش

    private boolean hasCriticalTasks;     // اگر کار فوری داره
}

