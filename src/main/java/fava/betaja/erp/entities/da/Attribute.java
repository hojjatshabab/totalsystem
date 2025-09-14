package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.BaseEntity;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.enums.AttributeDataType;
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
@Table(name = "attribute", schema = "da",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "key", "organization_unit_id"})
        })
public class Attribute extends BaseEntity {

    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    @Size(max = 255, message = "نام نباید بیشتر از ۲۵۵ کاراکتر باشد")
    @Column(name = "name", nullable = false, length = 255)
    @Comment("نام")
    private String name;

    @NotBlank(message = "کد الزامی است")
    @Size(max = 100, message = "کد نباید بیشتر از ۱۰۰ کاراکتر باشد")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "کد فقط می‌تواند شامل حروف، اعداد، خط تیره و زیرخط باشد")
    @Column(name = "key", nullable = false, length = 100)
    @Comment("کلید")
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    @Comment("نوع داده")
    private AttributeDataType dataType;


    @NotNull(message = "یگان الزامی است")
    @JoinColumn(name = "organization_unit_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @Comment("یگان")
    private OrganizationUnit organizationUnit;
}
