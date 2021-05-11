package com.senla.srs.mapper;

import com.senla.srs.dto.scooter.ScooterFullResponseDTO;
import com.senla.srs.entity.RentalSession;
import com.senla.srs.entity.Scooter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScooterFullResponseMapper extends AbstractMapperWithPagination<Scooter, ScooterFullResponseDTO>{
    private final RentalSessionCompactResponseMapper rentalSessionCompactResponseMapper;

    public ScooterFullResponseMapper(RentalSessionCompactResponseMapper rentalSessionCompactResponseMapper) {
        super(Scooter.class, ScooterFullResponseDTO.class);
        this.rentalSessionCompactResponseMapper = rentalSessionCompactResponseMapper;
    }

    public ScooterFullResponseDTO toFullDto(Scooter entity, List<RentalSession> rentalSessions) {
        var dto = super.toDto(entity);
        dto.setRentalSessions(rentalSessions.stream()
                .map(rentalSessionCompactResponseMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }
}
