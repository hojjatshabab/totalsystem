package fava.betaja.erp.entities.baseinfo;

import fava.betaja.erp.entities.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_storage", schema = "base_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"file_code"})}
)
public class FileStorage extends AbstractAuditingEntity {

    @Column(name = "file_code", columnDefinition = "TEXT", nullable = false, unique = true)
    @NotBlank(message = "لطفاً کد فایل را وارد کنید")
    @Comment("کد یکتا برای فایل")
    private String fileCode;

    @Column(name = "file_name", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "لطفاً نام فایل را وارد کنید")
    @Comment("نام فایل")
    private String fileName;

    @Column(name = "file_type", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "لطفاً نوع فایل را وارد کنید")
    @Comment("نوع فایل (PDF, DOCX, IMAGE, ...)")
    private String fileType;

    @Column(name = "files_path", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "لطفاً مسیر فایل را وارد کنید")
    @Comment("مسیر فیزیکی یا URL فایل")
    private String filesPath;

    @Column(name = "size", nullable = false)
    @NotNull(message = "سایز فایل الزامی است")
    @Comment("سایز فایل بر حسب بایت")
    private Long size;

    @Column(name = "record_id", columnDefinition = "TEXT")
    @Comment("شناسه رکورد یا موجودیت مرتبط (Cartable یا سایر موجودیت‌ها)")
    private String recordId;

    @Column(name = "key", columnDefinition = "TEXT")
    @Comment("کلید اختیاری برای شناسایی یا رمزنگاری")
    private String key;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("توضیحات فایل")
    private String description;
}
