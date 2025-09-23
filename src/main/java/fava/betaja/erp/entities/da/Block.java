package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.AbstractAuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "block", schema = "da",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "project_id"})
        })
public class Block extends AbstractAuditingEntity {

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

    @DecimalMin(value = "0.0", inclusive = false, message = "متراژ کلی باید بیشتر از صفر باشد")
    @Column(name = "total_area", precision = 18, scale = 2, nullable = false)
    @Comment("متراژ کلی (مترمربع)")
    private BigDecimal totalArea;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @NotNull(message = "تاریخ شروع الزامی است")
    @Column(name = "start_date", nullable = false)
    @Comment("تاریخ شروع")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @Column(name = "delivery_date")
    @Comment("تاریخ تحویل")
    private LocalDate deliveryDate;

    @NotNull(message = "پروژه الزامی هست")
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @Comment("پروژه")
    private Project project;
}
