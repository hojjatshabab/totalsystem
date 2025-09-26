package fava.betaja.erp.service.common.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.PageResponse;
import fava.betaja.erp.dto.common.OrganizationUnitDto;
import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.mapper.common.OrganizationUnitDtoMapper;
import fava.betaja.erp.repository.common.CommonBaseDataRepository;
import fava.betaja.erp.repository.common.CommonBaseTypeRepository;
import fava.betaja.erp.repository.common.OrganizationUnitRepository;
import fava.betaja.erp.service.common.OrganizationUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationUnitServiceImpl implements OrganizationUnitService {


    private final OrganizationUnitRepository organizationUnitRepository;
    private final OrganizationUnitDtoMapper organizationUnitDtoMapper;

    @Override
    public OrganizationUnitDto save(OrganizationUnitDto organizationUnitDto) {
        log.info("Save new organizationUnitDto {} in to database.", organizationUnitDto.getName());
        organizationUnitDto.setCodePath(generateCodePath(organizationUnitDto));
        return setChildrenOrganizationDto(organizationUnitDtoMapper
                .toDto(organizationUnitRepository.save(organizationUnitDtoMapper.toEntity(organizationUnitDto))));
    }

    @Override
    public OrganizationUnitDto getCurrentOrganizationUnit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "کاربر لاگین نشده است");
        }

        Users user = (Users) authentication.getPrincipal();
        OrganizationUnit organizationUnit = user.getOrganizationUnit();

        if (organizationUnit == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "یگان کاربر پیدا نشد");
        }

        return organizationUnitDtoMapper.toDto(organizationUnit);
    }

    @Override
    public List<OrganizationUnitDto> findAllList() {
        List<OrganizationUnit> organizationUnits = organizationUnitRepository.findAll();
        if (organizationUnits.isEmpty()) {
            return new ArrayList<>();
        }
        return organizationUnitDtoMapper.toDtoList(organizationUnits);
    }

    @Override
    public Optional<OrganizationUnitDto> findParentByOrgId(Long id) {
        if (Objects.nonNull(id)) {
            Optional<OrganizationUnit> unit = organizationUnitRepository.findById(id);
            if (unit.isPresent())
                if (Objects.nonNull(unit.get().getParent()))
                    return Optional.ofNullable(organizationUnitDtoMapper.toDto(unit.get().getParent()));
        }
        return Optional.empty();
    }

    @Override
    public OrganizationUnitDto update(OrganizationUnitDto organizationUnitDto) {
        log.info("Update organizationUnitDto {} in to database.", organizationUnitDto.getName());
        return setChildrenOrganizationDto(organizationUnitDtoMapper.toDto(organizationUnitRepository
                .save(organizationUnitDtoMapper.toEntity(organizationUnitDto))));
    }

    @Override
    public PageResponse<OrganizationUnitDto> findAll(PageRequest<OrganizationUnitDto> model) {
        log.info("Finding all organizationUnitDto.");
        List<OrganizationUnitDto> result = organizationUnitRepository.findAll(
                        Pageable.ofSize(model.getPageSize())
                                .withPage(model.getCurrentPage() - 1))
                .stream().map(organizationUnitDtoMapper::toDto).collect(Collectors.toList());
        long count = organizationUnitRepository.count();
        return new PageResponse<>(result, model.getPageSize(), count, model.getCurrentPage(), model.getSortBy());
    }

    @Override
    public OrganizationUnitDto findByParentIdIsNull() {
        return setChildrenOrganizationDto(organizationUnitDtoMapper
                .toDto(organizationUnitRepository.findByParentIdIsNull()));
    }

    @Override
    public Optional<OrganizationUnitDto> findById(Long id) {
        log.info("Finding organizationUnitDto by id {} .", id);
        Optional<OrganizationUnit> organizationUnitDtoOptional = organizationUnitRepository.findById(id);
        if (organizationUnitDtoOptional.isPresent()) {
            //  return Optional.ofNullable(setChildrenOrganizationDto(organizationUnitDtoMapper.toDto(organizationUnitDtoOptional.get())));
            return Optional.ofNullable(organizationUnitDtoMapper.toDto(organizationUnitDtoOptional.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<OrganizationUnitDto> findByName(String name) {
        log.info("Finding OrganizationUnitDto by name {} .", name);
        Optional<OrganizationUnit> organizationUnitOptional = organizationUnitRepository.findByName(name);
        if (organizationUnitOptional.isPresent()) {
            return Optional.ofNullable(setChildrenOrganizationDto(organizationUnitDtoMapper.toDto(organizationUnitOptional.get())));
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<OrganizationUnitDto>> findByCode(String code) {
        log.info("Finding OrganizationUnitDto by code {} .", code);
        Optional<List<OrganizationUnit>> organizationUnitOptional = organizationUnitRepository.findByCode(code);
        if (organizationUnitOptional.isPresent()) {
            List<OrganizationUnitDto> unitsDto = organizationUnitDtoMapper.toDtoList(organizationUnitOptional.get());
            List<OrganizationUnitDto> returnList = new ArrayList<>();
            for (OrganizationUnitDto dto : unitsDto)
                returnList.add(setChildrenOrganizationDto(dto));
            return Optional.ofNullable(returnList);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<OrganizationUnitDto>> findByNameContains(String name) {
        Optional<List<OrganizationUnit>> organizationUnit = organizationUnitRepository.findByNameContains(name);
        if (organizationUnit.get().size() > 0)
            return Optional.ofNullable(setChildrenOrganizationDto(organizationUnitDtoMapper.toDtoList(organizationUnit.get())));
        return Optional.empty();
    }

    @Override
    public Optional<List<OrganizationUnitDto>> findAllParentByOrgId(Long id) {
        List<OrganizationUnitDto> organizationUnitDtos = new ArrayList<>();
        while (Objects.nonNull(id) && findParentByOrgId(id).isPresent()) {
            OrganizationUnitDto dto = findById(findParentByOrgId(id).get().getId()).get();
            organizationUnitDtos.add(dto);
            if (Objects.nonNull(dto.getId()))
                id = dto.getId();
            else id = null;
        }
        return Optional.ofNullable(organizationUnitDtos);
    }

    @Override
    public Optional<List<OrganizationUnitDto>> findAllChildrenById(Long id) {
        log.info("Finding OrganizationUnitDto by id {} .", id);
        Optional<OrganizationUnit> organizationUnitOptional = organizationUnitRepository.findById(id);
        Optional<List<OrganizationUnitDto>> organizationUnitOptionalList = findAllChildrenByCodePath
                (organizationUnitOptional.get().getCodePath());
        if (organizationUnitOptionalList.isPresent()) {
            return organizationUnitOptionalList;
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<OrganizationUnitDto>> findAllChildrenByCodePath(String codePath) {
        log.info("Finding OrganizationUnitDto by codePath {} .", codePath);
        Optional<List<OrganizationUnit>> organizationUnitOptionalList = organizationUnitRepository
                .findAllChildrenByCodePath(codePath.concat("%"));
        if (organizationUnitOptionalList.isPresent()) {
            List<OrganizationUnitDto> unitsDto = organizationUnitDtoMapper.toDtoList(organizationUnitOptionalList.get());
            List<OrganizationUnitDto> returnList = new ArrayList<>();
            for (OrganizationUnitDto dto : unitsDto)
                returnList.add(setChildrenOrganizationDto(dto));
            return Optional.ofNullable(returnList);
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteById(Long id) {
        if (id == null || !findById(id).isPresent()) {
            log.info("Can not find OrganizationUnitDto by id {} .", id);
            return false;
        }
        organizationUnitRepository.deleteById(id);
        log.info("Success delete OrganizationUnitDto by id {} .", id);
        return true;
    }

    @Override
    public String generateCodePath(OrganizationUnitDto organizationUnitDto) {
        if (Objects.nonNull(organizationUnitDto.getId()))
            if (organizationUnitDto.getId() == 1L)
                return null;
        if (!Objects.nonNull(organizationUnitDto.getParentId())) return null;
        return organizationUnitRepository.findUniqueCodePathByParentCodePath
                (findById(organizationUnitDto.getParentId()).get().getCodePath());
    }

    @Override
    public List<OrganizationUnitDto> findAllForceWithOutChildren() {
        return organizationUnitDtoMapper.toDtoList(organizationUnitRepository
                .findByParent(organizationUnitRepository.findByParentIdIsNull()));
    }

    @Override
    public List<OrganizationUnitDto> findAllForceByParentWithOutChildren() {
        OrganizationUnit organizationUnit = organizationUnitRepository.findByParentIdIsNull();
        List<OrganizationUnitDto> result = organizationUnitDtoMapper.toDtoList(organizationUnitRepository
                .findByParent(organizationUnit));
        result.add(organizationUnitDtoMapper.toDto(organizationUnit));
        return result;
    }

    @Override
    public OrganizationUnitDto getRootWithOutChildren() {
        return organizationUnitDtoMapper.toDto(organizationUnitRepository.findByParentIdIsNull());
    }

    @Override
    public List<OrganizationUnitDto> findByParentByIdWithOutChildren(Long id) {
        OrganizationUnit organizationUnit = organizationUnitRepository.findById(id).get();
        return organizationUnitDtoMapper.toDtoList(organizationUnitRepository.findByParent(organizationUnit));
    }

    @Override
    public Optional<List<OrganizationUnitDto>> findChildrenById(Long id) {
        log.info("Finding findChildrenById {} .", id);
        Optional<OrganizationUnit> organizationUnitOptional = organizationUnitRepository.findById(id);
        if (organizationUnitOptional.isPresent()) {
            List<OrganizationUnitDto> unitsDto = organizationUnitDtoMapper.toDtoList(organizationUnitRepository.findByParentIdOrderByCodePathAsc(organizationUnitOptional.get().getId()).get());
            List<OrganizationUnitDto> returnList = new ArrayList<>();
            for (OrganizationUnitDto dto : unitsDto)
                returnList.add(setChildrenOrganizationDto(dto));

            return Optional.ofNullable(returnList);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<OrganizationUnitDto>> findChildrenByCodePath(String codePath) {
        log.info("Finding findChildrenById {} .", codePath);
        Optional<OrganizationUnit> organizationUnitOptional = organizationUnitRepository.findByCodePath(codePath);
        if (organizationUnitOptional.isPresent()) {
            List<OrganizationUnitDto> unitsDto = organizationUnitDtoMapper.toDtoList(organizationUnitRepository.findByParentIdOrderByCodePathAsc(organizationUnitOptional.get().getId()).get());
            List<OrganizationUnitDto> returnList = new ArrayList<>();
            for (OrganizationUnitDto dto : unitsDto)
                returnList.add(setChildrenOrganizationDto(dto));
            return Optional.ofNullable(returnList);
        }
        return Optional.empty();
    }

    @Override
    public String generateCodePathByParentId(OrganizationUnitDto organizationUnitDto) {
        if (Objects.nonNull(organizationUnitDto)) {
            if (organizationUnitDto.getId() != null) {
                Optional<List<OrganizationUnit>> units = organizationUnitRepository.findByParentIdOrderByCodePathDesc(organizationUnitDto.getId());
                if (units.get().size() > 0) {
                    Integer intCodePath = Integer.valueOf(units.get().get(0).getCodePath().trim()) + 1;
                    return "00".concat(intCodePath.toString());
                } else return organizationUnitDto.getCodePath().concat("001");
            } else {
                Optional<List<OrganizationUnit>> units = organizationUnitRepository.findAllChildrenByCodePath(organizationUnitDto.getCodePath().concat("%"));

                if (units.isPresent()) {
                    Integer intCodePath = Integer.valueOf(units.get().get(units.get().size() - 1).getCodePath().trim()) + 1;
                    return "00".concat(intCodePath.toString());
                } else return organizationUnitDto.getCodePath().concat("001");

            }
        }
        return null;
    }


    private OrganizationUnitDto setChildrenOrganizationDto(OrganizationUnitDto organizationUnitDto) {
        if (organizationUnitDto.getId() != null) {
            Optional<List<OrganizationUnit>> childrenOrg = organizationUnitRepository.findByParentIdOrderByCodePathAsc(organizationUnitDto.getId());
            if (childrenOrg.get().size() > 0) {
                List<OrganizationUnitDto> organizationUnitsDto = organizationUnitDtoMapper.toDtoList(childrenOrg.get());
                organizationUnitDto.setChildren(organizationUnitsDto);
                for (OrganizationUnitDto dto : organizationUnitsDto) {
                    setChildrenOrganizationDto(dto);
                }
            } else organizationUnitDto.setChildren(null);
            return organizationUnitDto;
        }
        return null;
    }

    private List<OrganizationUnitDto> setChildrenOrganizationDto(List<OrganizationUnitDto> organizationUnitDto) {
        List<OrganizationUnitDto> returnList = new ArrayList<>();
        if (organizationUnitDto != null && organizationUnitDto.size() > 0)
            for (OrganizationUnitDto dto : organizationUnitDto) {
                returnList.add(setChildrenOrganizationDto(dto));
            }
        return returnList;
    }
}
