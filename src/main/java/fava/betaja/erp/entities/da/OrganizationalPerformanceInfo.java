package fava.betaja.erp.entities.da;

import fava.betaja.erp.entities.BaseEntity;
import fava.betaja.erp.entities.common.CommonData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organizational_performance_info", schema = "da")
public class OrganizationalPerformanceInfo extends BaseEntity {

    @Column(name = "action_count")
    private Long actionCount;

    @ManyToOne
    @JoinColumn(name = "common_data_action_id", referencedColumnName = "id")
    private CommonData commonDataAction;

    @ManyToOne
    @JoinColumn(name = "organizational_performance_id", referencedColumnName = "id")
    private OrganizationalPerformance organizationalPerformance;


}
