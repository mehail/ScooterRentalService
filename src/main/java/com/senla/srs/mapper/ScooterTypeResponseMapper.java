package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import com.senla.srs.model.ScooterType;
import org.springframework.stereotype.Component;

@Component
public class ScooterTypeResponseMapper extends AbstractMapper<ScooterType, ScooterTypeResponseDTO> {
    public ScooterTypeResponseMapper() {
        super(ScooterType.class, ScooterTypeResponseDTO.class);
    }
}
