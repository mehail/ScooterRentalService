package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.model.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketFullResponseMapper extends AbstractMapperWithPagination<SeasonTicket, SeasonTicketFullResponseDTO>{
    public SeasonTicketFullResponseMapper() {
        super(SeasonTicket.class, SeasonTicketFullResponseDTO.class);
    }
}
