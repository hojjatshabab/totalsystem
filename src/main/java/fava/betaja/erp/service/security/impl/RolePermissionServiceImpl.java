package fava.betaja.erp.service.security.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.RolePermissionDto;
import fava.betaja.erp.entities.security.RolePermission;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.security.RolePermissionDtoMapper;
import fava.betaja.erp.repository.security.PermissionRepository;
import fava.betaja.erp.repository.security.RolePermissionRepository;
import fava.betaja.erp.repository.security.RoleRepository;
import fava.betaja.erp.service.security.RolePermissionService;
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
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository repository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionDtoMapper mapper;

    @Override
    public RolePermissionDto save(RolePermissionDto dto) {
        validate(dto, true);
        log.info("Saving RolePermissionDto: {}", dto.getId());
        RolePermission entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public RolePermissionDto update(RolePermissionDto dto) {
        validate(dto, false);
        log.info("Updating RolePermissionDto: id={}", dto.getId());
        RolePermission entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<RolePermissionDto> findAll(PageRequest<RolePermissionDto> model) {
        List<RolePermissionDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<RolePermissionDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RolePermissionDto> findById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public List<RolePermissionDto> findByPermissionId(Long permissionId) {
        return repository.findByPermissionId(permissionId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RolePermissionDto> findByRoleId(Long roleId) {
        return repository.findByRoleId(roleId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RolePermissionDto> findByRoleIdAndPermissionId(Long roleId, Long permissionId) {
        return repository.findByRoleIdAndPermissionId(roleId, permissionId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        RolePermission rolePermission = repository.findById(id)
                .orElseThrow(() -> new ServiceException("نقش با این شناسه یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted RolePermissionDto: id={}", rolePermission.getId());
    }

    private void validate(RolePermissionDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("کاربر برای بروزرسانی موجود نیست.");
        }
        if (dto.getRoleId() == null) {
            throw new ServiceException("نقش (Role) الزامی است.");
        }
        if (dto.getPermissionId() == null) {
            throw new ServiceException("مجوز (Permission) الزامی است.");
        }
        if (!roleRepository.existsById(dto.getRoleId())) {
            throw new ServiceException("نقش انتخاب شده موجود نیست.");
        }
        if (!permissionRepository.existsById(dto.getPermissionId())) {
            throw new ServiceException("مجوز انتخاب شده موجود نیست.");
        }
    }
}