package com.senla.srs.mapper;

import com.senla.srs.dto.SeasonTicketResponseDTO;
import com.senla.srs.model.SeasonTicket;

public class SeasonTicketMapper extends AbstractMapper<SeasonTicket, SeasonTicketResponseDTO> {

    public SeasonTicketMapper() {
        super(SeasonTicket.class, SeasonTicketResponseDTO.class);
    }

}
