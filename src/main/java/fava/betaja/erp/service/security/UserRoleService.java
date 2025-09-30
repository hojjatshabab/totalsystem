package fava.betaja.erp.service.security;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.UserRoleDto;
import fava.betaja.erp.dto.security.UsersDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {

    UserRoleDto save(UserRoleDto userRoleDto);

    UserRoleDto update(UserRoleDto userRoleDto);

    PageResponse<UserRoleDto> findAll(PageRequest<UserRoleDto> model);

    List<UserRoleDto> findAll();

    Optional<UserRoleDto> findById(Long id);

    List<UserRoleDto> findByUserId(Long userId);

    List<UserRoleDto> findByRoleId(Long roleId);

    List<UserRoleDto> findByRoleIdAndUserId(Long roleId, Long userId);

    void deleteById(Long id);

}
