package com.senla.srs.mapper;

import com.senla.srs.dto.ScooterTypeDTO;
import com.senla.srs.model.ScooterType;

public class ScooterTypeMapper extends AbstractMapper<ScooterType, ScooterTypeDTO> {

    public ScooterTypeMapper() {
        super(ScooterType.class, ScooterTypeDTO.class);
    }

}
