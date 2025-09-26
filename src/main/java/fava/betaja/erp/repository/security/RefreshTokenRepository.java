package fava.betaja.erp.repository.security;

import fava.betaja.erp.entities.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

}
