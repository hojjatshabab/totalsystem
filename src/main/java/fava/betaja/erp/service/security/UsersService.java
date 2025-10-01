package fava.betaja.erp.service.security;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.UsersDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.Users;

import java.util.List;
import java.util.Optional;

public interface UsersService {

    UsersDto save(UsersDto usersDto);

    UsersDto update(UsersDto usersDto);

    PageResponse<UsersDto> findAll(PageRequest<UsersDto> model);

    List<UsersDto> findAll();

    Optional<UsersDto> findById(Long id);

    Optional<UsersDto> findByUsername(String username);

    void deleteById(Long id);

    OrganizationUnit getCurrentUserOrganizationUnit();

    Users getCurrentUser();

}
