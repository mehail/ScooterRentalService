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
        var dto = super.toDto(entity);

        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        System.out.println("entity = " + entity);
        System.out.println("entity.getScooterType().getId() = " + entity.getScooterType().getId());


        dto.setScooterTypeId(entity.getScooterType().getId());

        System.out.println("dto = " + dto);
        System.out.println("\n\n\n\n\n\n\n\n\n\n");

        return dto;
    }
}
