package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketResponseDTO;
import com.senla.srs.model.SeasonTicket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeasonTicketResponseMapper extends AbstractMapper<SeasonTicket, SeasonTicketResponseDTO>{
    public SeasonTicketResponseMapper() {
        super(SeasonTicket.class, SeasonTicketResponseDTO.class);
    }

    public List<SeasonTicketRequestDTO> listToDto(List<SeasonTicket> seasonTickets) {
        return seasonTickets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
