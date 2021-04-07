package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.model.Scooter;
import org.springframework.stereotype.Component;

@Component
public class ScooterMapper extends AbstractMapper<Scooter, ScooterDTO> {
    public ScooterMapper() {
        super(Scooter.class, ScooterDTO.class);
    }
}
