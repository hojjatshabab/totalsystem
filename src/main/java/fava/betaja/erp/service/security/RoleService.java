package fava.betaja.erp.service.security;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    RoleDto save(RoleDto roleDto);

    RoleDto update(RoleDto roleDto);

    PageResponse<RoleDto> findAll(PageRequest<RoleDto> model);

    List<RoleDto> findAll();

    Optional<RoleDto> findById(Long id);

    Optional<RoleDto> findByName(String name);

    void deleteById(Long id);

}
