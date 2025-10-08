package fava.betaja.erp.repository.security;

import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.repository.security.extra.UserExtraRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long>, UserExtraRepository {
    Optional<Users> findByUsername(String username);

}
