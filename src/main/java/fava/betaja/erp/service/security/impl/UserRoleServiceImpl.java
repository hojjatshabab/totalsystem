package fava.betaja.erp.service.security.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.UserRoleDto;
import fava.betaja.erp.entities.security.UserRole;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.security.UserRoleDtoMapper;
import fava.betaja.erp.repository.security.RoleRepository;
import fava.betaja.erp.repository.security.UserRepository;
import fava.betaja.erp.repository.security.UserRoleRepository;
import fava.betaja.erp.service.security.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleDtoMapper mapper;

    @Override
    public UserRoleDto save(UserRoleDto dto) {
        validate(dto, true);
        log.info("Saving UserRoleDto: {}", dto.getId());
        UserRole entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public UserRoleDto update(UserRoleDto dto) {
        validate(dto, false);
        log.info("Updating UserRoleDto: id={}", dto.getId());
        UserRole entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<UserRoleDto> findAll(PageRequest<UserRoleDto> model) {
        List<UserRoleDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<UserRoleDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserRoleDto> findById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public List<UserRoleDto> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRoleDto> findByRoleId(Long roleId) {
        return repository.findByRoleId(roleId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRoleDto> findByRoleIdAndUserId(Long roleId, Long userId) {
        return repository.findByRoleIdAndUserId(roleId, userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        UserRole userRole = repository.findById(id)
                .orElseThrow(() -> new ServiceException("کاربر با این شناسه یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted UserRoleDto: id={}", userRole.getId());
    }

    private void validate(UserRoleDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("کاربر برای بروزرسانی موجود نیست.");
        }
        if (dto.getRoleId() == null) {
            throw new ServiceException("نقش (Role) الزامی است.");
        }
        if (dto.getUserId() == null) {
            throw new ServiceException("کاربر (user) الزامی است.");
        }
        if (!roleRepository.existsById(dto.getRoleId())) {
            throw new ServiceException("نقش انتخاب شده موجود نیست.");
        }
        if (!userRepository.existsById(dto.getUserId())) {
            throw new ServiceException("کاربر انتخاب شده موجود نیست.");
        }
    }
}