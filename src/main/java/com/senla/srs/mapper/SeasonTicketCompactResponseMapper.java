package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketCompactResponseDTO;
import com.senla.srs.entity.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketCompactResponseMapper extends AbstractMapper<SeasonTicket, SeasonTicketCompactResponseDTO>{
    public SeasonTicketCompactResponseMapper() {
        super(SeasonTicket.class, SeasonTicketCompactResponseDTO.class);
    }

    @Override
    public SeasonTicketCompactResponseDTO toDto(SeasonTicket entity) {
        var dto = new SeasonTicketCompactResponseDTO();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setScooterTypeId(entity.getScooterType().getId());
        dto.setRemainingTime(entity.getRemainingTime());
        dto.setStartDate(entity.getStartDate());
        dto.setExpiredDate(entity.getExpiredDate());
        dto.setAvailableForUse(entity.getAvailableForUse());

        return dto;
    }
}
