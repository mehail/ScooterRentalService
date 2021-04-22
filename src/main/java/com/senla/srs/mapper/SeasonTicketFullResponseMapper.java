package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.model.RentalSession;
import com.senla.srs.model.SeasonTicket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeasonTicketFullResponseMapper extends AbstractMapper<SeasonTicket, SeasonTicketFullResponseDTO>{
    public SeasonTicketFullResponseMapper() {
        super(SeasonTicket.class, SeasonTicketFullResponseDTO.class);
    }

    public List<SeasonTicketFullResponseDTO> mapListToDtoList(List<SeasonTicket> seasonTickets) {
        return seasonTickets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
