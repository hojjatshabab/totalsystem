package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.AttributePeriodDto;
import fava.betaja.erp.entities.da.AttributePeriod;
import fava.betaja.erp.entities.da.PeriodRange;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.AttributePeriodDtoMapper;
import fava.betaja.erp.repository.da.AttributePeriodRepository;
import fava.betaja.erp.repository.da.AttributeRepository;
import fava.betaja.erp.repository.da.PeriodRangeRepository;
import fava.betaja.erp.service.da.AttributePeriodService;
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
public class AttributePeriodServiceImpl implements AttributePeriodService {

    private final AttributePeriodRepository repository;
    private final AttributeRepository attributeRepository;
    private final PeriodRangeRepository periodRangeRepository;
    private final AttributePeriodDtoMapper mapper;

    @Override
    public AttributePeriodDto save(AttributePeriodDto dto) {
        validate(dto, true);

        String periodRangeName = "";
        String attributeName = "";

        Integer durationDays = null;

        if (dto.getPeriodRangeId() != null) {
            Optional<PeriodRange> optionalPeriodRange = periodRangeRepository.findById(dto.getPeriodRangeId());
            if (optionalPeriodRange.isPresent()){
                periodRangeName = optionalPeriodRange.get().getName();
                durationDays = optionalPeriodRange.get().getDurationDays();
            }
        }

        if (dto.getAttributeId() != null) {
            attributeName = attributeRepository.findById(dto.getAttributeId())
                    .map(a -> a.getName())
                    .orElse("");
        }

        // ساخت title
        String title = "";
        if (!periodRangeName.isBlank()) {
            title += periodRangeName;
        }
        if (!attributeName.isBlank()) {
            if (!title.isEmpty()) {
                title += " - ";
            }
            title += attributeName;
        }
        dto.setTitle(title);

        // محاسبه endDate بر اساس startDate و durationDays
        if (dto.getStartDate() != null && durationDays != null) {
            dto.setEndDate(dto.getStartDate().plusDays(durationDays));
        }

        log.info("Saving AttributePeriod: title={}, startDate={}, endDate={}", dto.getTitle(), dto.getStartDate(), dto.getEndDate());

        AttributePeriod entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public AttributePeriodDto update(AttributePeriodDto dto) {
        validate(dto, false);
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
}
