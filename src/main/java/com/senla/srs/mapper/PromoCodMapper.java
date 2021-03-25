package com.senla.srs.mapper;

import com.senla.srs.dto.PromoCodResponseDTO;
import com.senla.srs.model.PromoCod;
import org.springframework.stereotype.Component;

@Component
public class PromoCodMapper extends AbstractMapper<PromoCod, PromoCodResponseDTO> {

    public PromoCodMapper() {
        super(PromoCod.class, PromoCodResponseDTO.class);
    }

}
