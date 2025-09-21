package fava.betaja.erp.repository.da;

import fava.betaja.erp.entities.da.Block;
import fava.betaja.erp.entities.da.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {
    Page<Block> findByProjectId(UUID projectId, Pageable pageable);
}
