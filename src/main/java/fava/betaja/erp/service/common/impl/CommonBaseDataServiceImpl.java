package fava.betaja.erp.service.common.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.CommonBaseDataDto;
import fava.betaja.erp.entities.common.CommonBaseData;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.common.CommonBaseDataDtoMapper;
import fava.betaja.erp.repository.common.CommonBaseDataRepository;
import fava.betaja.erp.service.common.CommonBaseDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommonBaseDataServiceImpl implements CommonBaseDataService {

    private final CommonBaseDataRepository repository;
    private final CommonBaseDataDtoMapper mapper;

    @Override
    public CommonBaseDataDto save(CommonBaseDataDto dto) {
        validate(dto, true);
        log.info("Saving CommonBaseData: {}", dto.getValue());
        CommonBaseData entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CommonBaseDataDto update(CommonBaseDataDto dto) {
        validate(dto, false);
        log.info("Updating CommonBaseData: id={}, value={}", dto.getId(), dto.getValue());
        CommonBaseData entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<CommonBaseDataDto> findAll(PageRequest<CommonBaseDataDto> model) {
        List<CommonBaseDataDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<CommonBaseData> findByCommonBaseTypeIdAndActiveTrue(Long commonBaseTypeId, PageRequest<CommonBaseDataDto> model) {
        List<CommonBaseData> result = repository
                .findByCommonBaseTypeIdAndActiveTrue(
                        commonBaseTypeId,
                        Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream().collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<CommonBaseData> findByCommonBaseTypeIdOrderByOrderNoAsc(Long commonBaseTypeId) {
        return repository.findByCommonBaseTypeIdOrderByOrderNoAsc(commonBaseTypeId);
    }

    @Override
    public List<CommonBaseData> findByCommonBaseTypeIdAndActiveTrue(Long commonBaseTypeId) {
        return repository.findByCommonBaseTypeIdAndActiveTrue(commonBaseTypeId);
    }

    @Override
    public PageResponse<CommonBaseData> findByValueContainsAndCommonBaseTypeIdAndActiveTrue(String value, Long commonBaseTypeId, PageRequest<CommonBaseDataDto> model) {
        List<CommonBaseData> result = repository
                .findByValueContainingIgnoreCaseAndCommonBaseTypeIdAndActiveTrue(
                        value, commonBaseTypeId,
                        Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream().collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<CommonBaseDataDto> findByCommonBaseTypeAndValueContain(Long commonBaseTypeId, String values) {
        return repository.findByCommonBaseTypeIdAndValueContainingIgnoreCase(commonBaseTypeId, values)
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommonBaseDataDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CommonBaseDataDto> findById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        CommonBaseData entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("CommonBaseData با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted CommonBaseData: id={}, value={}", entity.getId(), entity.getValue());
    }

    private void validate(CommonBaseDataDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("CommonBaseData برای بروزرسانی موجود نیست.");
        }

        if (dto.getValue() == null || dto.getValue().isBlank()) {
            throw new ServiceException("مقدار Value الزامی است.");
        }

        if (dto.getCommonBaseTypeId() == null) {
            throw new ServiceException("CommonBaseType الزامی است.");
        }
    }
}
