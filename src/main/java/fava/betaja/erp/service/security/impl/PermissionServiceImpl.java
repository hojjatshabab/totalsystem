package fava.betaja.erp.service.security.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.PermissionDto;
import fava.betaja.erp.entities.security.Permission;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.security.PermissionDtoMapper;
import fava.betaja.erp.repository.security.PermissionRepository;
import fava.betaja.erp.service.security.PermissionService;
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
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;
    private final PermissionDtoMapper mapper;

    @Override
    public PermissionDto save(PermissionDto dto) {
        validate(dto, true);
        log.info("Saving Permission: {}", dto.getName());
        Permission entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PermissionDto update(PermissionDto dto) {
        validate(dto, false);
        log.info("Updating Permission: id={}, PermissionName={}", dto.getId(), dto.getName());
        Permission entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<PermissionDto> findAll(PageRequest<PermissionDto> model) {
        List<PermissionDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<PermissionDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PermissionDto> findById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public Optional<PermissionDto> findByName(String name) {
        return repository.findByName(name).map(mapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        Permission permission = repository.findById(id)
                .orElseThrow(() -> new ServiceException("مجوز با این شناسه یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted User: id={}, roleName={}", permission.getId(), permission.getName());
    }

    private void validate(PermissionDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("مجوز برای بروزرسانی موجود نیست.");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام الزامی است.");
        }
    }
}
