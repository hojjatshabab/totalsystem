package fava.betaja.erp.repository.common;


import fava.betaja.erp.entities.common.CommonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonTypeRepository extends JpaRepository<CommonType, Long> {
    Page<CommonType> findCommonTypeByTypeNameContainingIgnoreCase(String typeName, Pageable page);
}
