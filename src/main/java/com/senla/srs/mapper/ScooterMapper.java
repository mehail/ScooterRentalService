package com.senla.srs.mapper;

import com.senla.srs.dto.ScooterDTO;
import com.senla.srs.model.Scooter;

public class ScooterMapper extends AbstractMapper<Scooter, ScooterDTO> {

    public ScooterMapper() {
        super(Scooter.class, ScooterDTO.class);
    }

}
