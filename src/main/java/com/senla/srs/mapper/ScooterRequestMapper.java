package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.entity.Scooter;
import com.senla.srs.entity.ScooterType;
import org.springframework.stereotype.Component;

@Component
public class ScooterRequestMapper extends AbstractMapper<Scooter, ScooterRequestDTO> {

    public ScooterRequestMapper() {
        super(Scooter.class, ScooterRequestDTO.class);
    }

    public Scooter toEntity(ScooterRequestDTO dto, ScooterType scooterType) {
        var scooter = super.toEntity(dto);
        scooter.setType(scooterType);

        return scooter;
    }

}
