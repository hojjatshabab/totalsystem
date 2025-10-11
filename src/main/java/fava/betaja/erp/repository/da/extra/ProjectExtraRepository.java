package fava.betaja.erp.repository.da.extra;

import fava.betaja.erp.entities.da.Project;

import java.util.List;

public interface ProjectExtraRepository {
    List<Project> findByOrganizationUnitId(Long id);

}
