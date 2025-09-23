package fava.betaja.erp.repository.common;


import fava.betaja.erp.entities.common.CommonBaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommonBaseTypeRepository extends JpaRepository<CommonBaseType, Long> {
    Optional<CommonBaseType> findByClassNameIgnoreCase(String className);

    Page<CommonBaseType> findByClassNameContainingIgnoreCaseOrTitleContainingIgnoreCase(String className, String title, Pageable pageable);

    Page<CommonBaseType> findByClassNameContainingIgnoreCaseAndTitleContainingIgnoreCase(String className, String title, Pageable pageable);

   List<CommonBaseType> findByTitleContainingIgnoreCase(String Title);
}
