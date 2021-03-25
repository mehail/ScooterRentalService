package com.senla.srs.mapper;

import com.senla.srs.dto.ScooterResponseDTO;
import com.senla.srs.model.Scooter;

public class ScooterMapper extends AbstractMapper<Scooter, ScooterResponseDTO> {

    public ScooterMapper() {
        super(Scooter.class, ScooterResponseDTO.class);
    }

}
