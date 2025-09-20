package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.ProgressPeriodDto;
import fava.betaja.erp.entities.da.PeriodRange;
import fava.betaja.erp.entities.da.ProgressPeriod;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.ProgressPeriodDtoMapper;
import fava.betaja.erp.repository.da.ProgressValueRepository;
import fava.betaja.erp.repository.da.PeriodRangeRepository;
import fava.betaja.erp.repository.da.ProgressPeriodRepository;
import fava.betaja.erp.service.da.ProgressPeriodService;
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
public class ProgressPeriodServiceImpl implements ProgressPeriodService {

    private final ProgressPeriodRepository repository;
    private final PeriodRangeRepository periodRangeRepository;
    private final ProgressValueRepository progressValueRepository;
    private final ProgressPeriodDtoMapper mapper;

    @Override
    public ProgressPeriodDto save(ProgressPeriodDto dto) {
        validate(dto, true);
        enrichDto(dto);

        log.info("Saving ProgressPeriod: title={}, startDate={}, endDate={}", dto.getTitle(), dto.getStartDate(), dto.getEndDate());

        ProgressPeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ProgressPeriodDto update(ProgressPeriodDto dto) {
        validate(dto, false);
        enrichDto(dto);

        log.info("Updating ProgressPeriodDto: id={}, title={}", dto.getId(), dto.getTitle());
        ProgressPeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<ProgressPeriodDto> findAll(PageRequest<ProgressPeriodDto> model) {
        List<ProgressPeriodDto> result = repository
                .findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<ProgressPeriodDto> findByReferenceId(UUID referenceId, PageRequest<ProgressPeriodDto> model) {
        List<ProgressPeriodDto> result = repository
                .findByReferenceId(referenceId,
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public BigDecimal getTotalValue(UUID progressPeriodId) {
        return progressValueRepository.sumValueByProgressPeriodId(progressPeriodId);
    }

    @Override
    public List<ProgressPeriodDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProgressPeriodDto> findById(UUID id) {
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

    private void validate(ProgressPeriodDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("AttributePeriod برای بروزرسانی موجود نیست.");
        }
        if (dto.getStartDate() == null) {
            throw new ServiceException("ویژگی (startDate) الزامی است.");
        }
        if (dto.getReferenceId() == null) {
            throw new ServiceException("ویژگی (ReferenceId) الزامی است.");
        }
        if (dto.getReferenceType() == null) {
            throw new ServiceException("ویژگی (ReferenceTyp) الزامی است.");
        }
        if (dto.getPeriodRangeId() == null) {
            throw new ServiceException("نوع دوره (periodRange) الزامی است.");
        }
        if (!periodRangeRepository.existsById(dto.getPeriodRangeId())) {
            throw new ServiceException("نوع دوره انتخاب شده موجود نیست.");
        }

    }

    private void enrichDto(ProgressPeriodDto dto) {
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
