package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.scooter.ScooterCompactResponseDTO;
import com.senla.srs.core.entity.Scooter;
import org.springframework.stereotype.Component;

@Component
public class ScooterCompactResponseMapper extends AbstractMapperWithPagination<Scooter, ScooterCompactResponseDTO> {

    public ScooterCompactResponseMapper() {
        super(Scooter.class, ScooterCompactResponseDTO.class);
    }

}
