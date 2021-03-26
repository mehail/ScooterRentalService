package com.senla.srs.mapper;

import com.senla.srs.dto.ScooterTypeDTO;
import com.senla.srs.model.ScooterType;
import org.springframework.stereotype.Component;

@Component
public class ScooterTypeMapper extends AbstractMapper<ScooterType, ScooterTypeDTO> {
    public ScooterTypeMapper() {
        super(ScooterType.class, ScooterTypeDTO.class);
    }
}
