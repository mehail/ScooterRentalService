package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketResponseDTO;
import com.senla.srs.model.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketResponseMapper extends AbstractMapper<SeasonTicket, SeasonTicketResponseDTO>{
    public SeasonTicketResponseMapper() {
        super(SeasonTicket.class, SeasonTicketResponseDTO.class);
    }
}
