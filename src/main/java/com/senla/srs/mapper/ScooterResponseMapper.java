package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.model.Scooter;
import org.springframework.stereotype.Component;

@Component
public class ScooterResponseMapper extends AbstractMapper<Scooter, ScooterResponseDTO> {
    public ScooterResponseMapper() {
        super(Scooter.class, ScooterResponseDTO.class);
    }
}
