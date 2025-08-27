package fava.betaja.erp.repository.common;


import fava.betaja.erp.entities.common.CommonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonDataRepository extends JpaRepository<CommonData, Long> {
}
