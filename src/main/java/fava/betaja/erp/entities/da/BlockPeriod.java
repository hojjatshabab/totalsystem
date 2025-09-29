package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.AbstractAuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "block_period", schema = "da")
public class BlockPeriod extends AbstractAuditingEntity {

    @Column(name = "title")
    @Comment("عنوان")
    private String title;

    @Column(name = "year")
    @Comment("سال")
    private String year;

    @Column(name = "is_active", nullable = false)
    @NotNull(message = "وضعیت فعال/غیرفعال باید مشخص شود")
    @Comment("فعال / غیرفعال")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_range_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "نوع دوره نمی‌تواند خالی باشد")
    @JsonIgnore
    @Comment("نوع دوره")
    private PeriodRange periodRange;

    @NotNull(message = "بلوک الزامی هست")
    @JoinColumn(name = "block_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @Comment("بلوک")
    private Block block;

}
