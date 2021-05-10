package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.entity.Scooter;
import org.springframework.stereotype.Component;

@Component
public class ScooterResponseMapper extends AbstractMapperWithPagination<Scooter, ScooterResponseDTO> {

    public ScooterResponseMapper() {
        super(Scooter.class, ScooterResponseDTO.class);
    }

}
