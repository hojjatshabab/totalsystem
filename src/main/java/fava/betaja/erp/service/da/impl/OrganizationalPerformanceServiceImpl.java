package fava.betaja.erp.service.da.impl;


import fava.betaja.erp.dto.da.OrganizationalPerformanceDto;
import fava.betaja.erp.entities.da.OrganizationalPerformance;
import fava.betaja.erp.mapper.da.OrganizationPerformanceMapper;
import fava.betaja.erp.repository.da.OrganizationalPerformanceRepository;
import fava.betaja.erp.service.da.OrganizationalPerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationalPerformanceServiceImpl implements OrganizationalPerformanceService {

    @Autowired
    private final OrganizationalPerformanceRepository repository;

    @Autowired
    OrganizationPerformanceMapper mapper;

    @Override
    public Page<OrganizationalPerformanceDto> findAll(Pageable model) {
        Page<OrganizationalPerformance> performers
                =repository.findAll(model);
        List<OrganizationalPerformance> performanceList= performers.stream().toList();
        List<OrganizationalPerformanceDto> performanceDtoList=mapper.toDtoList(performanceList);

        return new PageImpl<>(performanceDtoList, model, performers.getTotalElements());

    }

    @Override
    public OrganizationalPerformanceDto save(OrganizationalPerformanceDto organizationalPerformanceDto) {
        OrganizationalPerformance entity=mapper.toEntity(organizationalPerformanceDto);
        OrganizationalPerformance savedEntity= repository.save(entity);
        OrganizationalPerformanceDto savedDto=mapper.toDto(savedEntity);
        return savedDto;
    }

    @Override
    public Optional<OrganizationalPerformanceDto> findById(UUID id) {
        Optional<OrganizationalPerformance> dto =repository.findById(id);
        if (dto.isPresent()) {
            return Optional.ofNullable(mapper.toDto(dto.get()));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (id == null || !findById(id).isPresent()) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

}
