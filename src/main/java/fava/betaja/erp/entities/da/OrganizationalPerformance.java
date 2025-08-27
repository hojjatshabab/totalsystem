package fava.betaja.erp.entities.da;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.BaseEntity;
import fava.betaja.erp.entities.common.CommonData;
import fava.betaja.erp.entities.common.OrganizationUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organizational_performance", schema = "da")
public class OrganizationalPerformance extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "information_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "IR", timezone = "Asia/Tehran")
    private Date informationDate;

    @ManyToOne
    @JoinColumn(name = "common_base_data_information_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CommonData informationType;

    @ManyToOne
    @JoinColumn(name = "organization_unit_id", referencedColumnName = "id", insertable = false, updatable = false)
    private OrganizationUnit organizationUnit;

    @OneToMany(mappedBy = "organizationalPerformance")
    @JsonIgnore
    private List<OrganizationalPerformanceInfo> organizationalPerformanceInfoList;

}
