package fava.betaja.erp.entities.da;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import fava.betaja.erp.enums.baseinfo.ActionTypeEnum;
import fava.betaja.erp.enums.da.RangeType;
import jakarta.persistence.*;
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
                @UniqueConstraint(columnNames = {"key","name","range_type"})
        })
public class PeriodRange extends AbstractAuditingEntity {

    @NotBlank(message = "نام بازه الزامی است")
    @Size(max = 100, message = "نام نباید بیشتر از ۱۰۰ کاراکتر باشد")
    @Column(name = "name", nullable = false, length = 100, unique = true)
    @Comment("نام بازه")
    private String name;

    @Column(name = "order_no")
    @Comment("شماره ترتیب نمایش")
    private Integer orderNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "range_type")
    @Comment("نوع بازه")
    private RangeType rangeType;

    @NotBlank(message = "کلید بازه الزامی است")
    @Size(max = 50, message = "کلید نباید بیشتر از ۵۰ کاراکتر باشد")
    @Pattern(regexp = "^[a-z0-9_-]+$", message = "کلید باید فقط شامل حروف کوچک انگلیسی، عدد، خط تیره یا زیرخط باشد")
    @Column(name = "key", nullable = false, length = 50, unique = true)
    @Comment("کلید یکتا")
    private String key;

    @NotNull(message = "مدت زمان بازه الزامی است")
    @Column(name = "duration_days", nullable = false)
    @Comment("مدت بازه به روز")
    private Integer durationDays;
}
