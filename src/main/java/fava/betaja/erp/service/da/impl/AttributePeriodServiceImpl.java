package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributePeriodDto;
import fava.betaja.erp.entities.da.AttributePeriod;
import fava.betaja.erp.entities.da.PeriodRange;
import fava.betaja.erp.entities.da.Attribute;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.AttributePeriodDtoMapper;
import fava.betaja.erp.repository.da.AttributePeriodRepository;
import fava.betaja.erp.repository.da.AttributeRepository;
import fava.betaja.erp.repository.da.AttributeValueRepository;
import fava.betaja.erp.repository.da.PeriodRangeRepository;
import fava.betaja.erp.service.da.AttributePeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AttributePeriodServiceImpl implements AttributePeriodService {

    private final AttributePeriodRepository repository;
    private final AttributeRepository attributeRepository;
    private final PeriodRangeRepository periodRangeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributePeriodDtoMapper mapper;

    @Override
    public AttributePeriodDto save(AttributePeriodDto dto) {
        validate(dto, true);
        enrichDto(dto);

        log.info("Saving AttributePeriod: title={}, startDate={}, endDate={}", dto.getTitle(), dto.getStartDate(), dto.getEndDate());

        AttributePeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public AttributePeriodDto update(AttributePeriodDto dto) {
        validate(dto, false);
        enrichDto(dto);

        log.info("Updating AttributePeriod: id={}, title={}", dto.getId(), dto.getTitle());
        AttributePeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<AttributePeriodDto> findAll(PageRequest<AttributePeriodDto> model) {
        List<AttributePeriodDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<AttributePeriodDto> findByAttributeId(UUID attributeId, PageRequest<AttributePeriodDto> model) {
        List<AttributePeriodDto> result = repository
                .findByAttributeId(attributeId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public BigDecimal getTotalValue(UUID attributePeriodId) {
        return attributeValueRepository.sumValueByAttributePeriodId(attributePeriodId);
    }

    @Override
    public List<AttributePeriodDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttributePeriodDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        repository.findById(id).orElseThrow(() ->
                new ServiceException("AttributePeriod با این id یافت نشد: " + id)
        );
        repository.deleteById(id);
        log.info("Deleted AttributePeriod: id={}", id);
    }

    private void validate(AttributePeriodDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("AttributePeriod برای بروزرسانی موجود نیست.");
        }

        if (dto.getStartDate() == null) {
            throw new ServiceException("ویژگی (startDate) الزامی است.");
        }
        if (dto.getAttributeId() == null) {
            throw new ServiceException("ویژگی (attribute) الزامی است.");
        }

        if (dto.getPeriodRangeId() == null) {
            throw new ServiceException("نوع دوره (periodRange) الزامی است.");
        }

        if (!periodRangeRepository.existsById(dto.getPeriodRangeId())) {
            throw new ServiceException("نوع دوره انتخاب شده موجود نیست.");
        }

        if (!attributeRepository.existsById(dto.getAttributeId())) {
            throw new ServiceException("ویژگی انتخاب شده موجود نیست.");
        }
    }
    private void enrichDto(AttributePeriodDto dto) {
        // مقداردهی نام‌ها و مدت زمان
        String periodRangeName = "";
        String attributeName = "";
        Integer durationDays = null;

        if (dto.getPeriodRangeId() != null) {
            periodRangeRepository.findById(dto.getPeriodRangeId()).ifPresent(p -> {
                dto.setPeriodRangeName(p.getName());
                dto.setEndDate(null); // اگه لازم باشه مقدار قبلی پاک بشه
                dto.setPeriodRangeId(p.getId());
                if (p.getDurationDays() != null) {
                    dto.setEndDate(dto.getStartDate() != null ? dto.getStartDate().plusDays(p.getDurationDays()) : null);
                }
            });
            durationDays = periodRangeRepository.findById(dto.getPeriodRangeId())
                    .map(PeriodRange::getDurationDays)
                    .orElse(null);
            periodRangeName = periodRangeRepository.findById(dto.getPeriodRangeId())
                    .map(PeriodRange::getName)
                    .orElse("");
        }

        if (dto.getAttributeId() != null) {
            attributeName = attributeRepository.findById(dto.getAttributeId())
                    .map(Attribute::getName)
                    .orElse("");
            dto.setAttributeName(attributeName);
        }

        // ساخت title (بر اساس periodRangeName + attributeName)
        StringBuilder titleBuilder = new StringBuilder();
        if (!periodRangeName.isBlank()) {
            titleBuilder.append(periodRangeName);
        }
        if (!attributeName.isBlank()) {
            if (titleBuilder.length() > 0) {
                titleBuilder.append(" - ");
            }
            titleBuilder.append(attributeName);
        }
        dto.setTitle(titleBuilder.toString());

        // محاسبه endDate بر اساس startDate و durationDays
        if (dto.getStartDate() != null && durationDays != null) {
            dto.setEndDate(dto.getStartDate().plusDays(durationDays));
        }
    }

}
