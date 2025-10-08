package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.AbstractAuditingEntity;
import fava.betaja.erp.enums.da.BlockValueState;
import fava.betaja.erp.enums.da.RangeType;
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
@Table(name = "block_value", schema = "da",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"project_period_id","block_id"})
        })
public class BlockValue extends AbstractAuditingEntity {

    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    @Size(max = 255, message = "نام نباید بیشتر از ۲۵۵ کاراکتر باشد")
    @Column(name = "name", nullable = false, length = 255)
    @Comment("نام")
    private String name;

    @Min(value = 1, message = "تعداد بلوک نمی‌تواند کمتر از ۱ باشد")
    @Column(name = "block_count", nullable = false)
    @Comment("تعداد بلوک")
    private Integer blockCount;

    @Min(value = 1, message = "تعداد طبقات نمی‌تواند کمتر از ۱ باشد")
    @Column(name = "floor_count", nullable = false)
    @Comment("تعداد طبقه")
    private Integer floorCount;

    @Min(value = 1, message = "تعداد واحدها نمی‌تواند کمتر از ۱ باشد")
    @Column(name = "unit_count", nullable = false)
    @Comment("تعداد واحد")
    private Integer unitCount;

    @Column(name = "total_area", nullable = false)
    @Comment("متراژ کلی (مترمربع)")
    private BigDecimal totalArea;

    @Column(name = "useful_area")
    @Comment("متراژ مفید")
    private BigDecimal usefulArea;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @NotNull(message = "تاریخ شروع الزامی است")
    @Column(name = "start_date", nullable = false)
    @Comment("تاریخ شروع")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @Column(name = "delivery_date")
    @Comment("تاریخ تحویل")
    private LocalDate deliveryDate;

    @Column(name = "progress_actual", nullable = false)
    @Comment("پیشرفت واقعی")
    private BigDecimal progressActual;

    @Column(name = "progress_plan", nullable = false)
    @Comment("پیشرفت برنامه ای")
    private BigDecimal progressPlan;

    @Column(name = "deviation", nullable = false)
    @Comment("انحراف معیار")
    private BigDecimal deviation;

    @Column(name = "progress_actual_in_period", nullable = false)
    @Comment("پیشرفت واقعی در ماه")
    private BigDecimal progressActualInPeriod;

    @Column(name = "progress_plan_in_period", nullable = false)
    @Comment("پیشرفت برنامه ای در ماه")
    private BigDecimal progressPlanInPeriod;

    @Size(max = 1000, message = "خلاصه فعالیت انجام شده نباید بیشتر از ۱۰۰۰ کاراکتر باشد")
    @Column(name = "activity_summary", length = 1000)
    @Comment("خلاصه فعالیت انجام شده")
    private String activitySummary;

    @Size(max = 1000, message = "خلاصه مشکلات و موانع نباید بیشتر از ۱۰۰۰ کاراکتر باشد")
    @Column(name = "issue_summary", length = 1000)
    @Comment("خلاصه مشکلات و موانع")
    private String issueSummary;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_value_state")
    @Comment("وضعیت تایید")
    private BlockValueState blockValueState;

    @NotNull(message = "دوره پروژه الزامی هست")
    @JoinColumn(name = "project_period_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @Comment("دوره پروژه")
    private ProjectPeriod projectPeriod;

    @NotNull(message = "بلوک الزامی هست")
    @JoinColumn(name = "block_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @Comment("بلوک")
    private Block block;
}
