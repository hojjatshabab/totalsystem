package fava.betaja.erp.entities.common;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "common_base_type")
public class CommonBaseType  {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Basic
    @NotBlank(message = "نام کلاس نمی‌تواند خالی باشد")
    @Size(max = 255, message = "نام کلاس نباید بیشتر از ۲۵۵ کاراکتر باشد")
    @Column(name = "class_name", nullable = false, unique = true, updatable = false, length = 255)
    @Comment("نام یکتا برای کلاس (جهت استفاده در سیستم)")
    private String className;

    @NotNull(message = "وضعیت فعال/غیرفعال الزامی است")
    @Column(name = "active", nullable = false)
    @Comment("وضعیت فعال بودن")
    private Boolean active = true;

    @NotBlank(message = "عنوان نمی‌تواند خالی باشد")
    @Size(max = 255, message = "عنوان نباید بیشتر از ۲۵۵ کاراکتر باشد")
    @Column(name = "persian_name", nullable = false, length = 255)
    @Comment("عنوان فارسی")
    private String title;

    @Size(max = 500, message = "توضیحات نباید بیشتر از ۵۰۰ کاراکتر باشد")
    @Column(name = "description", length = 500)
    @Comment("توضیحات تکمیلی")
    private String description;

}
