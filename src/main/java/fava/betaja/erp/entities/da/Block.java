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

    @Column(name = "block_count")
    @Comment("تعداد بلوک")
    private Integer blockCount;

    @Column(name = "floor_count")
    @Comment("تعداد طبقه")
    private Integer floorCount;

    @Column(name = "unit_count")
    @Comment("تعداد واحد")
    private Integer unitCount;

    @Column(name = "total_area")
    @Comment("متراژ کلی (مترمربع)")
    private BigDecimal totalArea;

    @Column(name = "useful_area")
    @Comment("متراژ مفید")
    private BigDecimal usefulArea;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "IR", timezone = "Asia/Tehran")
    @Column(name = "start_date")
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
