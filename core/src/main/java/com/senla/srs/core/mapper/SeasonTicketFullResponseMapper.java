package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.core.entity.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketFullResponseMapper extends AbstractMapperWithPagination<SeasonTicket, SeasonTicketFullResponseDTO>{

    public SeasonTicketFullResponseMapper() {
        super(SeasonTicket.class, SeasonTicketFullResponseDTO.class);
    }

}
