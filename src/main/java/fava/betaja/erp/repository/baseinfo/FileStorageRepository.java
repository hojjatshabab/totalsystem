package fava.betaja.erp.repository.baseinfo;

import fava.betaja.erp.entities.baseinfo.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, UUID> {
    List<FileStorage> findByRecordId(String recordId);

    Optional<FileStorage> findByFileCode(String fileCode);

    Optional<FileStorage> findByRecordIdAndKey(String recordId, String key);

    Optional<FileStorage> findByFileName(String fileName);
}
