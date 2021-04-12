package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.model.Scooter;
import com.senla.srs.service.ScooterTypeService;
import org.springframework.stereotype.Component;

@Component
public class ScooterRequestMapper extends AbstractMapper<Scooter, ScooterRequestDTO> {
    private final ScooterTypeService scooterTypeService;

    public ScooterRequestMapper(ScooterTypeService scooterTypeService) {
        super(Scooter.class, ScooterRequestDTO.class);
        this.scooterTypeService = scooterTypeService;
    }

    @Override
    public Scooter toEntity(ScooterRequestDTO dto) {
        Scooter scooter = super.toEntity(dto);
        scooter.setType(scooterTypeService.retrieveScooterTypeById(dto.getPointOfRentalId()).get());
        return scooter;
    }
}
