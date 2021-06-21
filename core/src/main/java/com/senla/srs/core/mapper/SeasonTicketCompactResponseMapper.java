package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.seasonticket.SeasonTicketCompactResponseDTO;
import com.senla.srs.core.entity.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketCompactResponseMapper extends AbstractMapper<SeasonTicket, SeasonTicketCompactResponseDTO>{

    public SeasonTicketCompactResponseMapper() {
        super(SeasonTicket.class, SeasonTicketCompactResponseDTO.class);
    }

    @Override
    public SeasonTicketCompactResponseDTO toDto(SeasonTicket entity) {
        var dto = super.toDto(entity);
        dto.setScooterTypeId(entity.getScooterType().getId());

        return dto;
    }
}
