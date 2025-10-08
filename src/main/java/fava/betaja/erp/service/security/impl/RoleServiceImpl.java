package fava.betaja.erp.service.security.impl;


import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.RoleDto;
import fava.betaja.erp.entities.security.Role;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.security.RoleDtoMapper;
import fava.betaja.erp.repository.security.RoleRepository;
import fava.betaja.erp.service.security.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleDtoMapper mapper;

    @Override
    public RoleDto save(RoleDto dto) {
        validate(dto, true);
        log.info("Saving Role: {}", dto.getName());
        Role entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public RoleDto update(RoleDto dto) {
        validate(dto, false);
        log.info("Updating Role: id={}, roleName={}", dto.getId(), dto.getName());
        Role entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<RoleDto> findAll(PageRequest<RoleDto> model) {
        List<RoleDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<RoleDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDto> findById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public Optional<RoleDto> findByName(String name) {
        return repository.findByName(name).map(mapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new ServiceException("نقش با این شناسه یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted User: id={}, roleName={}", role.getId(), role.getName());
    }

    private void validate(RoleDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("نقش برای بروزرسانی موجود نیست.");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام الزامی است.");
        }
    }
}
