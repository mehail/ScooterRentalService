package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.entity.RentalSession;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RentalSessionResponseMapper extends AbstractMapperWithPagination<RentalSession, RentalSessionResponseDTO> {

    private final SeasonTicketCompactResponseMapper seasonTicketCompactResponseMapper;

    public RentalSessionResponseMapper(SeasonTicketCompactResponseMapper seasonTicketCompactResponseMapper) {
        super(RentalSession.class, RentalSessionResponseDTO.class);
        this.seasonTicketCompactResponseMapper = seasonTicketCompactResponseMapper;
    }

    @Override
    public RentalSessionResponseDTO toDto(RentalSession entity) {
        RentalSessionResponseDTO dto = super.toDto(entity);

        dto.setBegin(LocalDateTime.of(entity.getBeginDate(), entity.getBeginTime()));

        if (entity.getEndDate() != null && entity.getEndTime() != null) {
            dto.setEnd(LocalDateTime.of(entity.getEndDate(), entity.getEndTime()));
        }

        if (entity.getSeasonTicket() != null) {
            dto.setSeasonTicket(seasonTicketCompactResponseMapper.toDto(entity.getSeasonTicket()));
        }

        return dto;
    }

}
