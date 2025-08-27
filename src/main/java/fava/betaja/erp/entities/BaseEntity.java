package fava.betaja.erp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.security.Users;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Configurable
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    @Column(name = "created_by", nullable = true, length = 50, updatable = false)
    @JsonIgnore
    private Long createdBy;
    @Column(name = "modified_by", length = 50)
    @JsonIgnore
    private Long modifiedBy;
    @CreatedDate
    @Column(name = "creation_datetime", updatable = false)
    @JsonIgnore
    private Instant creationDateTime = Instant.now();
    @LastModifiedDate
    @Column(name = "modification_datetime")
    @JsonIgnore
    private Instant modificationDateTime = Instant.now();

    @Column(name = "log_extra_info")
    private String logExtraInfo;

    @PrePersist
    @PreUpdate
    public void insertCreationAndModification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) authentication.getPrincipal();
//            String username = details.getUsername();
            Long userId = ((Users) details).getId();
            if (createdBy == null) {
                createdBy = Objects.isNull(userId) ? 3L : userId;
            }
            modifiedBy = userId;
        }
        if (createdBy == null) createdBy = 3L;


        this.logExtraInfo = "";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();


            if (request != null) {
                String userAgent = request.getHeader("User-Agent");
                this.logExtraInfo = userAgent != null ? userAgent : " Unknown Agent ";
                String ipAddress = request.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null || ipAddress.isEmpty()) {
                    ipAddress = request.getRemoteAddr();
                }
                this.logExtraInfo = logExtraInfo.concat(ipAddress != null ? " ip->".concat(ipAddress) : " no ip address ");

            } else {
                this.logExtraInfo = " No request available ";
            }

        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Instant getModificationDateTime() {
        return modificationDateTime;
    }

    public void setModificationDateTime(Instant modificationDateTime) {
        this.modificationDateTime = modificationDateTime;
    }



    public String getLogExtraInfo() {
        return logExtraInfo;
    }

    public void setLogExtraInfo(String logExtraInfo) {
        this.logExtraInfo = logExtraInfo;
    }
}