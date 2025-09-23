package fava.betaja.erp.service.common.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.CommonBaseTypeDto;
import fava.betaja.erp.entities.common.CommonBaseType;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.common.CommonBaseTypeDtoMapper;
import fava.betaja.erp.repository.common.CommonBaseTypeRepository;
import fava.betaja.erp.service.common.CommonBaseTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommonBaseTypeServiceImpl implements CommonBaseTypeService {

    private final CommonBaseTypeRepository repository;
    private final CommonBaseTypeDtoMapper mapper;

    @Override
    public CommonBaseTypeDto save(CommonBaseTypeDto dto) {
        validate(dto, true);
        log.info("Saving CommonBaseType: {}", dto.getTitle());
        CommonBaseType entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CommonBaseTypeDto update(CommonBaseTypeDto dto) {
        validate(dto, false);
        log.info("Updating CommonBaseType: id={}, title={}", dto.getId(), dto.getTitle());
        CommonBaseType entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<CommonBaseTypeDto> findAll(PageRequest<CommonBaseTypeDto> model) {
        List<CommonBaseTypeDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<CommonBaseTypeDto> findByClassNameContainingOrTitleContaining(String className, String title, PageRequest<CommonBaseTypeDto> model) {
        List<CommonBaseTypeDto> result = repository
                .findByClassNameContainingIgnoreCaseOrTitleContainingIgnoreCase(
                        className, title,
                        Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<CommonBaseTypeDto> findByClassNameContainingAndTitleContaining(String className, String title, PageRequest<CommonBaseTypeDto> model) {
        List<CommonBaseTypeDto> result = repository
                .findByClassNameContainingIgnoreCaseAndTitleContainingIgnoreCase(
                        className, title,
                        Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<CommonBaseTypeDto> findByTitleContains(String title) {
        return repository.findByTitleContainingIgnoreCase(title)
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommonBaseTypeDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CommonBaseTypeDto> findById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public Optional<CommonBaseTypeDto> findByClassName(String className) {
        return repository.findByClassNameIgnoreCase(className)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        CommonBaseType entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("CommonBaseType با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted CommonBaseType: id={}, title={}", entity.getId(), entity.getTitle());
    }

    private void validate(CommonBaseTypeDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("CommonBaseType برای بروزرسانی موجود نیست.");
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new ServiceException("عنوان الزامی است.");
        }

        if (dto.getClassName() == null || dto.getClassName().isBlank()) {
            throw new ServiceException("نام کلاس الزامی است.");
        }
    }
}
