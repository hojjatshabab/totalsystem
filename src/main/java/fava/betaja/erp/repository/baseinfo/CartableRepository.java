package fava.betaja.erp.repository.baseinfo;

import fava.betaja.erp.entities.baseinfo.Cartable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartableRepository extends JpaRepository<Cartable, UUID> {
    List<Cartable> findByRecipientIdOrderBySendDateDesc(Long recipientId);
    List<Cartable> findByCurrentStepId(UUID currentStepId);
    List<Cartable> findByState(String state);
}
