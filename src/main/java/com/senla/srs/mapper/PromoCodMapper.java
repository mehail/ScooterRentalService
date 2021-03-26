package com.senla.srs.mapper;

import com.senla.srs.dto.PromoCodDTO;
import com.senla.srs.model.PromoCod;
import org.springframework.stereotype.Component;

@Component
public class PromoCodMapper extends AbstractMapper<PromoCod, PromoCodDTO> {
    public PromoCodMapper() {
        super(PromoCod.class, PromoCodDTO.class);
    }
}
