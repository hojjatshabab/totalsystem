package fava.betaja.erp.entities.da;


import fava.betaja.erp.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "period_type", schema = "da")
public class PeriodType extends BaseEntity {


    @Column(name = "name")
    private String name;

    @Column(name = "key")
    private String key;

    @Column(name = "duration_days")
    private Integer durationDays;

}
