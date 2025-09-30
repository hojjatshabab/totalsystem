package fava.betaja.erp.service.security;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.PermissionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

public interface PermissionService {

    PermissionDto save(PermissionDto permissionDto);

    PermissionDto update(PermissionDto permissionDto);

    PageResponse<PermissionDto> findAll(PageRequest<PermissionDto> model);

    List<PermissionDto> findAll();

    Optional<PermissionDto> findById(Long id);

    Optional<PermissionDto> findByName(String name);

    void deleteById(Long id);

}