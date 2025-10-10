package fava.betaja.erp.repository.baseinfo;

import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.enums.baseinfo.CartableState;
import fava.betaja.erp.repository.baseinfo.extra.CartableExtraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartableRepository extends JpaRepository<Cartable, UUID>, CartableExtraRepository {
    List<Cartable> findByRecipientIdOrderBySendDateDesc(Long recipientId);

    Page<Cartable> findByRecipientIdAndStateNot(Long recipientId, CartableState state, Pageable pageable);

    List<Cartable> findByCurrentStepId(UUID currentStepId);

    List<Cartable> findByState(String state);

}
