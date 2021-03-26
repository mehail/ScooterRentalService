package com.senla.srs.mapper;

import com.senla.srs.dto.SeasonTicketDTO;
import com.senla.srs.model.SeasonTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketMapper extends AbstractMapper<SeasonTicket, SeasonTicketDTO> {
    @Autowired
    public SeasonTicketMapper() {
        super(SeasonTicket.class, SeasonTicketDTO.class);
    }
}
