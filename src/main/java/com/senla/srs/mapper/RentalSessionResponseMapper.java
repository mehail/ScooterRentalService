package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.model.RentalSession;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RentalSessionResponseMapper extends AbstractMapper<RentalSession, RentalSessionResponseDTO>{

    public RentalSessionResponseMapper() {
        super(RentalSession.class, RentalSessionResponseDTO.class);
    }

    public List<RentalSessionResponseDTO> mapListToDtoList(List<RentalSession> rentalSessions) {
        return rentalSessions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
