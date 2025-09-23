package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.BaseEntity;
import fava.betaja.erp.entities.common.OrganizationUnit;
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
@Table(name = "project", schema = "da",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "plan_id"})
        })
public class Project extends BaseEntity {

    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    @Size(max = 255, message = "نام نباید بیشتر از ۲۵۵ کاراکتر باشد")
    @Column(name = "name", nullable = false, length = 255)
    @Comment("نام")
    private String name;

    @NotNull(message = "پلن الزامی هست")
    @JoinColumn(name = "plan_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @Comment("پلن")
    private Plan plan;
}
