package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.ScooterCompactResponseDTO;
import com.senla.srs.entity.Scooter;
import org.springframework.stereotype.Component;

@Component
public class ScooterResponseMapper extends AbstractMapperWithPagination<Scooter, ScooterCompactResponseDTO> {

    public ScooterResponseMapper() {
        super(Scooter.class, ScooterCompactResponseDTO.class);
    }

}
