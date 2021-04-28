package com.senla.srs.mapper;

import com.senla.srs.dto.db.MakerDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.model.ScooterType;
import org.springframework.stereotype.Component;

@Component
public class ScooterTypeRequestMapper extends AbstractMapper<ScooterType, ScooterTypeRequestDTO> {
    public ScooterTypeRequestMapper() {
        super(ScooterType.class, ScooterTypeRequestDTO.class);
    }

    public ScooterType toEntity(ScooterTypeRequestDTO dto, MakerDTO makerDTO) {
        ScooterType scooterType = super.toEntity(dto);

        scooterType.setMaker(makerDTO);

        return scooterType;
    }
}