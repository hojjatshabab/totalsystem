package fava.betaja.erp.entities.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.AbstractAuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "common_base_data",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"value", "common_base_type_id"})
        }
)
public class CommonBaseData  {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull(message = "وضعیت فعال/غیرفعال الزامی است")
    @Column(name = "is_active", nullable = false)
    @Comment("وضعیت فعال بودن")
    private Boolean active = true;

    @NotBlank(message = "مقدار نمی‌تواند خالی باشد")
    @Size(max = 255, message = "مقدار نباید بیشتر از ۲۵۵ کاراکتر باشد")
    @Column(name = "value", nullable = false, length = 255)
    @Comment("مقدار داده (یونیک در هر نوع)")
    private String value;

    @Size(max = 500, message = "توضیحات نباید بیشتر از ۵۰۰ کاراکتر باشد")
    @Column(name = "description", length = 500)
    @Comment("توضیحات تکمیلی")
    private String description;

    @Column(name = "order_no")
    @Comment("شماره ترتیب نمایش")
    private Integer orderNo;

    @NotNull(message = "نوع داده الزامی است")
    @JoinColumn(name = "common_base_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @Comment("نوع پایه مرتبط")
    private CommonBaseType commonBaseType;
}

