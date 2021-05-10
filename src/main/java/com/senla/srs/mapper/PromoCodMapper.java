package com.senla.srs.mapper;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.entity.PromoCod;
import org.springframework.stereotype.Component;

@Component
public class PromoCodMapper extends AbstractMapperWithPagination<PromoCod, PromoCodDTO> {

    public PromoCodMapper() {
        super(PromoCod.class, PromoCodDTO.class);
    }

}
