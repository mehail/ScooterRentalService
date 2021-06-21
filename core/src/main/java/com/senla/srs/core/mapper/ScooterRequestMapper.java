package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.scooter.ScooterRequestDTO;
import com.senla.srs.core.entity.Scooter;
import com.senla.srs.core.entity.ScooterType;
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
