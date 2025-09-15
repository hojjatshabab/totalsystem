package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributeValueDto;
import fava.betaja.erp.entities.da.AttributeValue;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.AttributeValueDtoMapper;
import fava.betaja.erp.repository.da.AttributePeriodRepository;
import fava.betaja.erp.repository.da.AttributeValueRepository;
import fava.betaja.erp.repository.da.PeriodRangeRepository;
import fava.betaja.erp.service.da.AttributeValueService;
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
public class AttributeValueServiceImpl implements AttributeValueService {

    private final AttributeValueRepository repository;
    private final AttributePeriodRepository attributePeriodRepository;
    private final PeriodRangeRepository periodRangeRepository;
    private final AttributeValueDtoMapper mapper;

    @Override
    public AttributeValueDto save(AttributeValueDto dto) {
        validate(dto, true);
        calculateEndDate(dto);

        log.info("Saving AttributeValue for AttributePeriodId={}", dto.getAttributePeriodId());
        AttributeValue entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public AttributeValueDto update(AttributeValueDto dto) {
        validate(dto, false);
        calculateEndDate(dto);

        log.info("Updating AttributeValue: id={}, AttributePeriodId={}", dto.getId(), dto.getAttributePeriodId());
        AttributeValue entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }


    @Override
    public PageResponse<AttributeValueDto> findAll(PageRequest<AttributeValueDto> model) {
        List<AttributeValueDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<AttributeValueDto> findByAttributePeriodId(UUID attributePeriodId, PageRequest<AttributeValueDto> model) {
        List<AttributeValueDto> result = repository
                .findByAttributePeriodId(attributePeriodId, Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<AttributeValueDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttributeValueDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        AttributeValue entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("AttributeValue با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted AttributeValue: id={}, AttributePeriodId={}", entity.getId(), entity.getAttributePeriod().getId());
    }

    private void validate(AttributeValueDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("AttributeValue برای بروزرسانی موجود نیست.");
        }

        if (dto.getAttributePeriodId() == null) {
            throw new ServiceException("AttributePeriod الزامی است.");
        }

        if (!attributePeriodRepository.existsById(dto.getAttributePeriodId())) {
            throw new ServiceException("AttributePeriod انتخاب شده موجود نیست.");
        }

        if (dto.getPeriodRangeId() == null) {
            throw new ServiceException("نوع دوره (periodRange) الزامی است.");
        }

        if (!periodRangeRepository.existsById(dto.getPeriodRangeId())) {
            throw new ServiceException("نوع دوره انتخاب شده موجود نیست.");
        }

        if (dto.getValue() == null) {
            throw new ServiceException("مقدار (value) الزامی است.");
        }

        if (dto.getStartDate() == null) {
            throw new ServiceException("ویژگی (startDate) الزامی است.");
        }
    }

    /**
     * متدی برای محاسبه endDate بر اساس startDate و periodRange.durationDays
     */
    private void calculateEndDate(AttributeValueDto dto) {
        if (dto.getPeriodRangeId() != null) {
            periodRangeRepository.findById(dto.getPeriodRangeId())
                    .ifPresent(p -> {
                        Integer durationDays = p.getDurationDays();
                        if (dto.getStartDate() != null && durationDays != null) {
                            dto.setEndDate(dto.getStartDate().plusDays(durationDays));
                        }
                    });
        }
    }
}