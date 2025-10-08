package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.ProgressValueDto;
import fava.betaja.erp.entities.da.ProgressValue;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.ProgressValueDtoMapper;
import fava.betaja.erp.repository.da.ProgressPeriodRepository;
import fava.betaja.erp.repository.da.ProgressValueRepository;
import fava.betaja.erp.repository.da.PeriodRangeRepository;
import fava.betaja.erp.service.da.ProgressValueService;
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
public class ProgressValueServiceImpl implements ProgressValueService {

    private final ProgressValueRepository repository;
    private final ProgressPeriodRepository progressPeriodRepository;
    private final PeriodRangeRepository periodRangeRepository;
    private final ProgressValueDtoMapper mapper;

    @Override
    public ProgressValueDto save(ProgressValueDto dto) {
        validate(dto, true);
        calculateEndDate(dto);

        log.info("Saving ProgressValue for ProgressPeriodId = {}", dto.getProgressPeriodId());
        ProgressValue entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ProgressValueDto update(ProgressValueDto dto) {
        validate(dto, false);
        calculateEndDate(dto);

        log.info("Updating ProgressValue: id = {}, ProgressPeriodId = {}", dto.getId(), dto.getProgressPeriodId());
        ProgressValue entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }


    @Override
    public PageResponse<ProgressValueDto> findAll(PageRequest<ProgressValueDto> model) {
        List<ProgressValueDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public PageResponse<ProgressValueDto> findByProgressValueId(UUID progressValueId, PageRequest<ProgressValueDto> model) {
        List<ProgressValueDto> result = repository
                .findByProgressPeriodId(progressValueId, Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<ProgressValueDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProgressValueDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        ProgressValue entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("ProgressValue با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted ProgressValue: id = {}, ProgressValue = {}", entity.getId(), entity.getProgressPeriod().getId());
    }

    private void validate(ProgressValueDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("ProgressValue برای بروزرسانی موجود نیست.");
        }

        if (dto.getProgressPeriodId() == null) {
            throw new ServiceException("ProgressPeriod الزامی است.");
        }

        if (!progressPeriodRepository.existsById(dto.getProgressPeriodId())) {
            throw new ServiceException("ProgressPeriod انتخاب شده موجود نیست.");
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
    private void calculateEndDate(ProgressValueDto dto) {
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