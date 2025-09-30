package fava.betaja.erp.service.security.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.security.UsersDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.security.UsersDtoMapper;
import fava.betaja.erp.repository.common.OrganizationUnitRepository;
import fava.betaja.erp.repository.security.UserRepository;
import fava.betaja.erp.service.security.UsersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UserRepository repository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UsersDtoMapper mapper;

    @Override
    public UsersDto save(UsersDto dto) {
        validate(dto, true);
        log.info("Saving User: {}", dto.getUsername());
        Users entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public UsersDto update(UsersDto dto) {
        validate(dto, false);
        log.info("Updating User: id={}, username={}", dto.getId(), dto.getUsername());
        Users entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<UsersDto> findAll(PageRequest<UsersDto> model) {
        List<UsersDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<UsersDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsersDto> findById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public Optional<UsersDto> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        Users user = repository.findById(id)
                .orElseThrow(() -> new ServiceException("کاربر با این شناسه یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted User: id={}, username={}", user.getId(), user.getUsername());
    }

    @Override
    public OrganizationUnit getCurrentUserOrganizationUnit() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Users user) {
           return user.getOrganizationUnit();
        }
        return null;
    }

    private void validate(UsersDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("کاربر برای بروزرسانی موجود نیست.");
        }

        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new ServiceException("نام کاربری الزامی است.");
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new ServiceException("رمز عبور الزامی است.");
        }

        if (dto.getOrganizationUnitId() == null) {
            throw new ServiceException("یگان (OrganizationUnit) الزامی است.");
        }

        if (!organizationUnitRepository.existsById(dto.getOrganizationUnitId())) {
            throw new ServiceException("یگان انتخاب شده موجود نیست.");
        }
    }
}
