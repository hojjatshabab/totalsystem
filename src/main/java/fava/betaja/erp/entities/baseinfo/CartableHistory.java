package fava.betaja.erp.entities.baseinfo;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.enums.baseinfo.ActionTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cartable_history", schema = "base_info")
public class CartableHistory extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cartable_id", referencedColumnName = "id", nullable = false)
    @Comment("کارتابل مربوطه")
    private Cartable cartable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("کاربر اقدام کننده")
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    @Comment("نوع اقدام")
    private ActionTypeEnum actionType;

    @Column(name = "comment", length = 1000)
    @Comment("توضیح یا یادداشت")
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "action_date", nullable = false)
    @Comment("تاریخ و زمان اقدام")
    private Date actionDate = new Date();
}

