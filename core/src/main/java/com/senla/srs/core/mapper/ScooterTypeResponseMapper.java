package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.scooter.type.ScooterTypeResponseDTO;
import com.senla.srs.core.entity.ScooterType;
import org.springframework.stereotype.Component;

@Component
public class ScooterTypeResponseMapper extends AbstractMapperWithPagination<ScooterType, ScooterTypeResponseDTO> {

    public ScooterTypeResponseMapper() {
        super(ScooterType.class, ScooterTypeResponseDTO.class);
    }

}
