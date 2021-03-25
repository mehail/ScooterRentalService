package com.senla.srs.mapper;

import com.senla.srs.dto.ScooterTypeResponseDTO;
import com.senla.srs.model.ScooterType;

public class ScooterTypeMapper extends AbstractMapper<ScooterType, ScooterTypeResponseDTO> {

    public ScooterTypeMapper() {
        super(ScooterType.class, ScooterTypeResponseDTO.class);
    }

}
