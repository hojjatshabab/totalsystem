package fava.betaja.erp.entities.baseinfo;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.enums.baseinfo.CartableState;
import fava.betaja.erp.enums.baseinfo.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cartable", schema = "base_info",
        uniqueConstraints = @UniqueConstraint(columnNames = {"document_id", "sender_id","flow_rule_domain_id","recipient_id"}))
public class Cartable extends AbstractAuditingEntity {

    @Column(name = "title", nullable = false, length = 500)
    @Comment("عنوان کارتابل")
    private String title;

    @Column(name = "document_id")
    @Comment("شناسه یکتا سند/مستند مرتبط")
    private UUID documentId;

    @Column(name = "document_number")
    @Comment("شماره پیگیری کارتابل")
    private String documentNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 50)
    @Comment("وضعیت کارتابل")
    private CartableState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @Comment("فرستنده")
    private Users sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    @Comment("گیرنده")
    private Users recipient;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("توضیحات/هامش")
    private String description;

    @Column(name = "send_date")
    @Comment("تاریخ ارسال")
    private LocalDate sendDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flow_rule_domain_id", referencedColumnName = "id", nullable = false)
    @Comment("دامنه جریان مربوطه")
    private FlowRuleDomain flowRuleDomain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_step_id", referencedColumnName = "id")
    @Comment("مرحله جاری")
    private FlowRuleStep currentStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_step_id", referencedColumnName = "id")
    @Comment("مرحله بعدی")
    private FlowRuleStep nextStep;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "priority", nullable = false)
    private Priority priority = Priority.NORMAL;

    @Column(name = "read", nullable = false)
    @Comment("خوانده شده/نشده")
    private Boolean read = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "due_date")
    @Comment("تاریخ سررسید")
    private Date dueDate;

}

