package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionFullResponseDTO;
import com.senla.srs.entity.RentalSession;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RentalSessionFullResponseMapper extends AbstractMapperWithPagination<RentalSession, RentalSessionFullResponseDTO> {

    private final SeasonTicketCompactResponseMapper seasonTicketCompactResponseMapper;

    public RentalSessionFullResponseMapper(SeasonTicketCompactResponseMapper seasonTicketCompactResponseMapper) {
        super(RentalSession.class, RentalSessionFullResponseDTO.class);
        this.seasonTicketCompactResponseMapper = seasonTicketCompactResponseMapper;
    }

    @Override
    public RentalSessionFullResponseDTO toDto(RentalSession entity) {
        RentalSessionFullResponseDTO dto = super.toDto(entity);

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
