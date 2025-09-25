package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleDto;
import fava.betaja.erp.entities.baseinfo.FlowRule;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.baseinfo.FlowRuleDtoMapper;
import fava.betaja.erp.repository.baseinfo.FlowRuleRepository;
import fava.betaja.erp.service.baseinfo.FlowRuleService;
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
public class FlowRuleServiceImpl implements FlowRuleService {

    private final FlowRuleRepository repository;
    private final FlowRuleDtoMapper mapper;

    @Override
    public FlowRuleDto save(FlowRuleDto dto) {
        validate(dto, true);
        log.info("Saving FlowRule: name={}", dto.getName());
        FlowRule entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public FlowRuleDto update(FlowRuleDto dto) {
        validate(dto, false);
        log.info("Updating FlowRule: id={}, name={}", dto.getId(), dto.getName());
        FlowRule entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<FlowRuleDto> findAll(PageRequest<FlowRuleDto> model) {
        List<FlowRuleDto> result = repository.findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1)).stream().map(mapper::toDto).collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<FlowRuleDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public FlowRuleDto findByName(String name) {
        FlowRule entity = repository.findByName(name);
        if (entity == null) {
            return new FlowRuleDto();
        }
        return mapper.toDto(entity);
    }

    @Override
    public List<FlowRuleDto> findByActiveTrue() {
        return repository.findByActiveTrue().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<FlowRuleDto> findById(UUID id) {
        return Optional.ofNullable(id).flatMap(repository::findById).map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        FlowRule flowRule = repository.findById(id).orElseThrow(() -> new ServiceException("FlowRule با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted FlowRule: id={}, name={}", flowRule.getId(), flowRule.getName());
    }

    private void validate(FlowRuleDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("FlowRule برای بروزرسانی موجود نیست.");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ServiceException("نام FlowRule الزامی است.");
        }
    }
}
