package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.promocod.PromoCodDTO;
import com.senla.srs.core.entity.PromoCod;
import org.springframework.stereotype.Component;

@Component
public class PromoCodMapper extends AbstractMapperWithPagination<PromoCod, PromoCodDTO> {

    public PromoCodMapper() {
        super(PromoCod.class, PromoCodDTO.class);
    }

}
