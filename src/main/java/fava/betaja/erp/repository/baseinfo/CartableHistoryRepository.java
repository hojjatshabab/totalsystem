package fava.betaja.erp.repository.baseinfo;

import fava.betaja.erp.entities.baseinfo.CartableHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface CartableHistoryRepository extends JpaRepository<CartableHistory, UUID> {
    List<CartableHistory> findByCartableIdOrderByCreationDateTimeDesc(UUID cartableId);
    List<CartableHistory> findByCartableIdOrderByCreationDateTimeAsc(UUID cartableId);
    CartableHistory findTopByUserIdOrderByCreationDateTimeDesc(Long userId);
}
