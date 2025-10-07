package fava.betaja.erp.service.baseinfo.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.baseinfo.CartableHistoryDto;
import fava.betaja.erp.entities.baseinfo.CartableHistory;
import fava.betaja.erp.exceptions.ServiceException;
import fava.betaja.erp.mapper.baseinfo.CartableHistoryDtoMapper;
import fava.betaja.erp.repository.baseinfo.CartableHistoryRepository;
import fava.betaja.erp.service.baseinfo.CartableHistoryService;
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
public class CartableHistoryServiceImpl implements CartableHistoryService {

    private final CartableHistoryRepository repository;
    private final CartableHistoryDtoMapper mapper;

    @Override
    public CartableHistoryDto save(CartableHistoryDto dto) {
        validate(dto, true);
        log.info("Saving CartableHistory for cartableId: {}", dto.getCartableId());
        CartableHistory entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PageResponse<CartableHistoryDto> findAll(PageRequest<CartableHistoryDto> model) {
        List<CartableHistoryDto> result = repository
                .findAll(Pageable.ofSize(model.getPageSize()).withPage(model.getCurrentPage() - 1))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        long count = result.size();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public List<CartableHistoryDto> findByCartableIdOrderByCreationDateTimeDesc(UUID cartableId) {
        return repository.findByCartableIdOrderByCreationDateTimeDesc(cartableId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartableHistoryDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartableHistoryDto> findById(UUID id) {
        return Optional.ofNullable(id)
                .flatMap(repository::findById)
                .map(mapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        CartableHistory history = repository.findById(id)
                .orElseThrow(() -> new ServiceException("CartableHistory با این id یافت نشد: " + id));
        repository.deleteById(id);
        log.info("Deleted CartableHistory: id={}", history.getId());
    }

    private void validate(CartableHistoryDto dto, boolean isCreate) {
        if (!isCreate && (dto.getId() == null || !repository.existsById(dto.getId()))) {
            throw new ServiceException("CartableHistory برای بروزرسانی موجود نیست.");
        }
        if (dto.getCartableId() == null) {
            throw new ServiceException("CartableId الزامی است.");
        }
        if (dto.getActionType() == null) {
            throw new ServiceException("getActionType الزامی است.");
        }
    }
}
