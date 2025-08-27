package fava.betaja.erp.service.common;

import fava.betaja.erp.dto.common.CommonDataDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommonDataService {
    Page<CommonDataDto> findAll(Pageable pageable);

    CommonDataDto save(CommonDataDto dto);

    public Optional<CommonDataDto> findById(Long id);


    public Boolean deleteById(Long id);

}
