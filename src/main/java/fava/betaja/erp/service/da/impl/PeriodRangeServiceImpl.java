package fava.betaja.erp.service.da.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.da.PeriodRangeDto;
import fava.betaja.erp.entities.da.PeriodRange;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.da.PeriodRangeDtoMapper;
import fava.betaja.erp.repository.da.PeriodRangeRepository;
import fava.betaja.erp.service.da.PeriodRangeService;
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
public class PeriodRangeServiceImpl implements PeriodRangeService {

    private final PeriodRangeRepository repository;
    private final PeriodRangeDtoMapper mapper;

    @Override
    public PeriodRangeDto save(PeriodRangeDto dto) {
        validate(dto, true);
        log.info("Saving PeriodRange: name={}", dto.getName());
        PeriodRange entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PeriodRangeDto update(PeriodRangeDto dto) {
        validate(dto, false);
        log.info("Updating PeriodRange: id={}, name={}", dto.getId(), dto.getName());
        PeriodRange entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<PeriodRangeDto> findAll(PageRequest<PeriodRangeDto> model) {
        List<PeriodRangeDto> dtos = repository
                .findAll(Pageable.ofSize(model.getPageSize())
                        .withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        long count = repository.count();
        return new PageResponse<>(dtos, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<PeriodRangeDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PeriodRangeDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        PeriodRange entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException("PeriodRange با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted PeriodRange: id={}, name={}", entity.getId(), entity.getName());
    }

    private void validate(PeriodRangeDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("PeriodRange برای بروزرسانی موجود نیست.");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام PeriodRange الزامی است.");
        }

        if (dto.getDurationDays() == null || dto.getDurationDays() <= 0) {
            throw new ServiceException("تعداد روزهای DurationDays باید بیشتر از صفر باشد.");
        }

        if (dto.getKey() == null || dto.getKey().isBlank()) {
            throw new ServiceException("کلید (Key) الزامی است.");
        }
    }
}
