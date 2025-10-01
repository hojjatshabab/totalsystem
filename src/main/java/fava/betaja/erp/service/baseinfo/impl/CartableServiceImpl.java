package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.baseinfo.CartableDtoMapper;
import fava.betaja.erp.repository.baseinfo.CartableRepository;
import fava.betaja.erp.service.baseinfo.CartableService;
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
public class CartableServiceImpl implements CartableService {

    private final CartableRepository repository;
    private final CartableDtoMapper mapper;

    @Override
    public CartableDto save(CartableDto dto) {
        validate(dto, true);
        StringBuilder title = new StringBuilder();
   /*     title.append(periodRangeRepository.findById(dto.getPeriodRangeId()).get().getName())
                .append("، ")
                .append(dto.getYear())
                .append(" - پروژه ")
                .append(projectRepository.findById(dto.getProjectId()).get().getName());
        dto.setTitle(title.toString());*/
        log.info("Saving Cartable: documentNumber={}", dto.getDocumentNumber());
        Cartable entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CartableDto update(CartableDto dto) {
        validate(dto, false);
        log.info("Updating Cartable: id={}, documentNumber={}", dto.getId(), dto.getDocumentNumber());
        Cartable entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<CartableDto> findAll(PageRequest<CartableDto> model) {
        List<CartableDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        long count = repository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<CartableDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartableDto> findByRecipientIdOrderBySendDateDesc(Long recipientId) {
        return repository.findByRecipientIdOrderBySendDateDesc(recipientId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartableDto> findByCurrentStepId(UUID currentStepId) {
        return repository.findByCurrentStepId(currentStepId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartableDto> findByState(String state) {
        return repository.findByState(state)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartableDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        Cartable cartable = repository.findById(id)
                .orElseThrow(() -> new ServiceException("Cartable با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted Cartable: id={}, documentNumber={}", cartable.getId(), cartable.getDocumentNumber());
    }

    private void validate(CartableDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("Cartable برای بروزرسانی موجود نیست.");
        }
        if (dto.getDocumentNumber() == null || dto.getDocumentNumber().isBlank()) {
            throw new ServiceException("DocumentNumber الزامی است.");
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new ServiceException("Title الزامی است.");
        }
        if (dto.getFlowRuleDomainId() == null) {
            throw new ServiceException("FlowRuleDomainId الزامی است.");
        }
        if (dto.getState() == null) {
            throw new ServiceException("State الزامی است.");
        }
    }
}
