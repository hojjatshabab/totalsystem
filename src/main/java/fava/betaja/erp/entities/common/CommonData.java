package fava.betaja.erp.entities.common;


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
@Table(name = "common_data")
public class CommonData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "key")
    private String key;

    @Column(name = "type_id")
    private String typeId;


    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", insertable = false,updatable = false)
    private CommonType commonType;



}
