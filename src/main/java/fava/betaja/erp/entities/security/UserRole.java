package fava.betaja.erp.entities.security;


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
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "role_id")
    private Long RoleId;


    @ManyToOne
    @JoinColumn(name = "user_id" ,referencedColumnName = "id",updatable = false,insertable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "role_id" ,referencedColumnName = "id",updatable = false,insertable = false)  // This is the foreign key column in Order table
    private Role role;

}
