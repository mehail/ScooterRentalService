package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.scooter.type.MakerDTO;
import com.senla.srs.core.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.core.entity.ScooterType;
import org.springframework.stereotype.Component;

@Component
public class ScooterTypeRequestMapper extends AbstractMapper<ScooterType, ScooterTypeRequestDTO> {

    public ScooterTypeRequestMapper() {
        super(ScooterType.class, ScooterTypeRequestDTO.class);
    }

    public ScooterType toEntity(ScooterTypeRequestDTO dto, MakerDTO makerDTO) {
        var scooterType = super.toEntity(dto);
        scooterType.setMaker(makerDTO);

        return scooterType;
    }

    public ScooterType toEntity(ScooterTypeRequestDTO dto, MakerDTO makerDTO, Long id) {
        var scooterType = toEntity(dto, makerDTO);
        scooterType.setId(id);

        return scooterType;
    }

}