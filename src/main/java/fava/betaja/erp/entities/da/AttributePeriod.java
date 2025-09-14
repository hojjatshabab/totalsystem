package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attribute_period", schema = "da")
public class AttributePeriod extends BaseEntity {

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "تاریخ شروع نمی‌تواند خالی باشد")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @Comment("تاریخ شروع")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull(message = "تاریخ پایان نمی‌تواند خالی باشد")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @Comment("تاریخ پایان")
    private LocalDate endDate;

    @AssertTrue(message = "تاریخ پایان باید بعد از تاریخ شروع باشد")
    @Transient
    private boolean isValidDateRange() {
        return startDate != null && endDate != null && !endDate.isBefore(startDate);
    }

    @Column(name = "is_active", nullable = false)
    @NotNull(message = "وضعیت فعال/غیرفعال باید مشخص شود")
    @Comment("فعال / غیرفعال")
    private Boolean isActive;

    @Column(name = "title")
    @Comment("عنوان")
    private String title;

    @Column(name = "value_planned", nullable = false, precision = 14, scale = 2)
    @NotNull(message = "مقدار پیش‌ بینی شده نمی‌تواند خالی باشد")
    @DecimalMin(value = "0.0", inclusive = false, message = "مقدار پیش ‌بینی باید صفر یا بیشتر باشد")
    @Comment("مقدار پیش‌ بینی شده")
    private BigDecimal valuePlanned;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "ویژگی نمی‌تواند خالی باشد")
    @JsonIgnore
    @Comment("ویژگی")
    private Attribute attribute;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_range_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "نوع دوره نمی‌تواند خالی باشد")
    @JsonIgnore
    @Comment("نوع دوره")
    private PeriodRange periodRange;

}
