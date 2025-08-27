package fava.betaja.erp.service.common;

import fava.betaja.erp.dto.common.CommonTypeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface CommonTypeService {


    Page<CommonTypeDto> findAll(Pageable pageable);

    Page<CommonTypeDto> findByName(Pageable pageable,String name);


    CommonTypeDto save(CommonTypeDto dto);

    public Optional<CommonTypeDto> findById(Long id);


    public Boolean deleteById(Long id);

}
