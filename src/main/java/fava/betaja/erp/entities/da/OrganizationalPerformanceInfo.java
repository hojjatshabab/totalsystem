package fava.betaja.erp.entities.da;

import fava.betaja.erp.entities.BaseEntity;
import fava.betaja.erp.entities.common.CommonData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "organizational_performance_info", schema = "da")
public class OrganizationalPerformanceInfo extends BaseEntity {

    @Column(name = "action_count")
    private Long actionCount;

    @Column(name = "common_data_action_id")
    private Long commonDataActionId;


    @Column(name = "file_info_id")
    private Long fileInfoId;


    @ManyToOne
    @JoinColumn(name = "common_data_action_id", referencedColumnName = "id", updatable = false, insertable = false)
    private CommonData commonDataAction;

    @ManyToOne
    @JoinColumn(name = "file_info_id", referencedColumnName = "id", updatable = false, insertable = false)
    private OrganizationalPerformance organizationalPerformance;


}
