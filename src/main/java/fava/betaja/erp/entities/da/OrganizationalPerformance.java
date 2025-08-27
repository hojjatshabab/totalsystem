package fava.betaja.erp.entities.da;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fava.betaja.erp.entities.BaseEntity;
import fava.betaja.erp.entities.common.CommonData;
import fava.betaja.erp.entities.common.OrganizationUnit;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "organizational_performance",schema = "da")
public class OrganizationalPerformance extends BaseEntity {

    @Column(name = "information_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "IR", timezone = "Asia/Tehran")
    private Date informationDate;

    private String description;

    @Column(name = "common_base_data_information_type_id")
    private Long informationTypeId;

    @Column(name = "organization_unit_id")
    private Long organizationUnitId;


    @ManyToOne
    @JoinColumn(name = "common_base_data_information_type_id",referencedColumnName = "id",insertable=false,updatable=false  )
    private CommonData informationType;

    @ManyToOne
    @JoinColumn(name = "organization_unit_id",referencedColumnName = "id",insertable=false,updatable=false  )
    private OrganizationUnit organizationUnit;



    @OneToMany(mappedBy = "organizationalPerformance")
    @JsonIgnore
    private List<OrganizationalPerformanceInfo> organizationalPerformanceInfoList;

}
