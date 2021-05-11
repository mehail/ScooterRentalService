package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.ScooterCompactResponseDTO;
import com.senla.srs.entity.Scooter;
import org.springframework.stereotype.Component;

@Component
public class ScooterCompactResponseMapper extends AbstractMapperWithPagination<Scooter, ScooterCompactResponseDTO> {

    public ScooterCompactResponseMapper() {
        super(Scooter.class, ScooterCompactResponseDTO.class);
    }

}
