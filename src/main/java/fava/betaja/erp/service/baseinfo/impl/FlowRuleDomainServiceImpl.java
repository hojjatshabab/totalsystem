package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.FlowRuleDomainDto;
import fava.betaja.erp.entities.baseinfo.FlowRuleDomain;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.baseinfo.FlowRuleDomainDtoMapper;
import fava.betaja.erp.repository.baseinfo.FlowRuleDomainRepository;
import fava.betaja.erp.service.baseinfo.FlowRuleDomainService;
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
public class FlowRuleDomainServiceImpl implements FlowRuleDomainService {

    private final FlowRuleDomainRepository repository;
    private final FlowRuleDomainDtoMapper mapper;

    @Override
    public FlowRuleDomainDto save(FlowRuleDomainDto dto) {
        validate(dto, true);
        log.info("Saving FlowRuleDomain: entityName={}, domain={}", dto.getEntityName(), dto.getDomain());
        FlowRuleDomain entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public FlowRuleDomainDto update(FlowRuleDomainDto dto) {
        validate(dto, false);
        log.info("Updating FlowRuleDomain: id={}, entityName={}", dto.getId(), dto.getEntityName());
        FlowRuleDomain entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<FlowRuleDomainDto> findAll(PageRequest<FlowRuleDomainDto> model) {
        List<FlowRuleDomainDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<FlowRuleDomainDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowRuleDomainDto> findByFlowRuleId(UUID flowRuleId) {
        return repository.findByFlowRuleId(flowRuleId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowRuleDomainDto> findByEntityName(String entityName) {
        return repository.findByEntityNameIgnoreCase(entityName).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FlowRuleDomainDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        FlowRuleDomain domain = repository.findById(id)
                .orElseThrow(() -> new ServiceException("FlowRuleDomain با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted FlowRuleDomain: id={}, entityName={}", domain.getId(), domain.getEntityName());
    }

    private void validate(FlowRuleDomainDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("FlowRuleDomain برای بروزرسانی موجود نیست.");
        }

        if (dto.getDomain() == null ) {
            throw new ServiceException("نام Domain الزامی است.");
        }

        if (dto.getEntityName() == null || dto.getEntityName().isBlank()) {
            throw new ServiceException("نام Entity الزامی است.");
        }
    }
}
