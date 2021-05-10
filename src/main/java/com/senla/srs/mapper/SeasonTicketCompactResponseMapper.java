package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketCompactResponseDTO;
import com.senla.srs.entity.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketCompactResponseMapper extends AbstractMapper<SeasonTicket, SeasonTicketCompactResponseDTO>{

    public SeasonTicketCompactResponseMapper() {
        super(SeasonTicket.class, SeasonTicketCompactResponseDTO.class);
    }

}
