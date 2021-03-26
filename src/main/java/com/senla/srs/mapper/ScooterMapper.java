package com.senla.srs.mapper;

import com.senla.srs.dto.ScooterDTO;
import com.senla.srs.model.Scooter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScooterMapper extends AbstractMapper<Scooter, ScooterDTO> {
    @Autowired
    public ScooterMapper() {
        super(Scooter.class, ScooterDTO.class);
    }
}
