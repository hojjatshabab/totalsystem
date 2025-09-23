package fava.betaja.erp.entities.da;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "period_range", schema = "da",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"key"}),
                @UniqueConstraint(columnNames = {"name"})
        })
public class PeriodRange extends AbstractAuditingEntity {

    @NotBlank(message = "نام بازه الزامی است")
    @Size(max = 100, message = "نام نباید بیشتر از ۱۰۰ کاراکتر باشد")
    @Column(name = "name", nullable = false, length = 100, unique = true)
    @Comment("نام بازه (مثلا ماهانه، سه‌ماهه، شش‌ماهه، سالانه)")
    private String name;

    @NotBlank(message = "کلید بازه الزامی است")
    @Size(max = 50, message = "کلید نباید بیشتر از ۵۰ کاراکتر باشد")
    @Pattern(regexp = "^[a-z0-9_-]+$", message = "کلید باید فقط شامل حروف کوچک انگلیسی، عدد، خط تیره یا زیرخط باشد")
    @Column(name = "key", nullable = false, length = 50, unique = true)
    @Comment("کلید یکتا (مثلا: monthly, quarterly, yearly)")
    private String key;

    @NotNull(message = "مدت زمان بازه الزامی است")
    @Column(name = "duration_days", nullable = false)
    @Comment("مدت بازه به روز (مثلا 30 برای ماهانه، 90 برای سه ماهه)")
    private Integer durationDays;
}
