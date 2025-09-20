package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "progress_value", schema = "da")
public class ProgressValue extends BaseEntity {

    @NotNull(message = "تاریخ شروع الزامی است")
    @Column(name = "start_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @Comment("تاریخ شروع")
    private LocalDate startDate;

    @NotNull(message = "تاریخ پایان الزامی است")
    @Column(name = "end_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @Comment("تاریخ پایان")
    private LocalDate endDate;

    @NotNull(message = "مقدار الزامی است")
    @Digits(integer = 12, fraction = 2, message = "مقدار باید حداکثر ۱۲ رقم صحیح و ۲ رقم اعشار داشته باشد")
    @Column(name = "value", nullable = false, precision = 14, scale = 2)
    @Comment("مقدار ثبت شده")
    private BigDecimal value;

    @Column(name = "description")
    @Comment("توضیحات")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_range_id", referencedColumnName = "id" ,nullable = false)
    @JsonIgnore
    @Comment("نوع بازه")
    private PeriodRange periodRange;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "progress_period_id", referencedColumnName = "id",nullable = false)
    @JsonIgnore
    @Comment("دوره پیشرفت")
    private ProgressPeriod progressPeriod;

}
