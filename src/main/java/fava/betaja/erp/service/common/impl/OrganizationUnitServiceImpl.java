package fava.betaja.erp.service.common.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.common.OrganizationUnitDtoMapper;
import fava.betaja.erp.repository.common.OrganizationUnitRepository;
import fava.betaja.erp.service.common.OrganizationUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationUnitServiceImpl implements OrganizationUnitService {

    private final OrganizationUnitRepository organizationUnitRepository;
    private final OrganizationUnitDtoMapper organizationUnitDtoMapper;

    @Override
    public OrganizationUnitDto save(OrganizationUnitDto dto) {
        validate(dto, true);
        log.info("Saving OrganizationUnit: {}", dto.getName());
        return organizationUnitDtoMapper.toDto(
                organizationUnitRepository.save(organizationUnitDtoMapper.toEntity(dto))
        );
    }

    @Override
    public OrganizationUnitDto update(OrganizationUnitDto dto) {
        validate(dto, false);
        log.info("Updating OrganizationUnit: {}", dto.getName());
        return organizationUnitDtoMapper.toDto(
                organizationUnitRepository.save(organizationUnitDtoMapper.toEntity(dto))
        );
    }

    @Override
    public OrganizationUnitDto getCurrentOrganizationUnit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "کاربر لاگین نشده است");
        }

        Users user = (Users) authentication.getPrincipal();
        OrganizationUnit organizationUnit = user.getOrganizationUnit();

        if (organizationUnit == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "یگان کاربر پیدا نشد");
        }

        return organizationUnitDtoMapper.toDto(organizationUnit);
    }

    @Override
    public PageResponse<OrganizationUnitDto> findAll(PageRequest<OrganizationUnitDto> model) {
        List<OrganizationUnitDto> result = organizationUnitRepository
                .findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream().map(organizationUnitDtoMapper::toDto)
                .collect(Collectors.toList());
        long count = organizationUnitRepository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<OrganizationUnitDto> findAll() {
        return organizationUnitRepository.findAll().stream()
                .map(organizationUnitDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrganizationUnitDto> findById(Long id) {
        if (id == null) {
            throw new ServiceException("شناسه یگان نمی‌تواند خالی باشد.");
        }
        return organizationUnitRepository.findById(id)
                .map(organizationUnitDtoMapper::toDto);
    }

    @Override
    public Boolean deleteById(Long id) {
        if (!findById(id).isPresent()) {
            throw new ServiceException("یگان مورد نظر پیدا نشد.");
        }
        organizationUnitRepository.deleteById(id);
        return true;
    }

    private void validate(OrganizationUnitDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !organizationUnitRepository.existsById(dto.getId()))) {
            throw new ServiceException("یگان مورد نظر برای بروزرسانی موجود نیست.");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام یگان الزامی است.");
        }
    }
}
