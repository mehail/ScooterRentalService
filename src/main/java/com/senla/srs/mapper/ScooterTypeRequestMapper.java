package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.model.ScooterType;
import com.senla.srs.service.MakerDtoService;
import org.springframework.stereotype.Component;

@Component
public class ScooterTypeRequestMapper extends AbstractMapper<ScooterType, ScooterTypeRequestDTO> {
    private final MakerDtoService makerDtoService;

    public ScooterTypeRequestMapper(MakerDtoService makerDtoService) {
        super(ScooterType.class, ScooterTypeRequestDTO.class);
        this.makerDtoService = makerDtoService;
    }

    //ToDo refactor
    @Override
    public ScooterType toEntity(ScooterTypeRequestDTO dto) {
        ScooterType scooterType = super.toEntity(dto);
        scooterType.setMaker(makerDtoService.retrieveMakerDtoById(dto.getMakerId()).get());

        return scooterType;
    }
}