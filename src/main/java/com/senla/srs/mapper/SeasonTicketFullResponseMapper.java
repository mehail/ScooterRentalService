package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.entity.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketFullResponseMapper extends AbstractMapperWithPagination<SeasonTicket, SeasonTicketFullResponseDTO>{
    private final ScooterTypeResponseMapper scooterTypeResponseMapper;

    public SeasonTicketFullResponseMapper(ScooterTypeResponseMapper scooterTypeResponseMapper) {
        super(SeasonTicket.class, SeasonTicketFullResponseDTO.class);
        this.scooterTypeResponseMapper = scooterTypeResponseMapper;
    }

    @Override
    public SeasonTicketFullResponseDTO toDto(SeasonTicket entity) {
        var dto = new SeasonTicketFullResponseDTO();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setScooterType(scooterTypeResponseMapper.toDto(entity.getScooterType()));
        dto.setRemainingTime(entity.getRemainingTime());
        dto.setStartDate(entity.getStartDate());
        dto.setExpiredDate(entity.getExpiredDate());
        dto.setAvailableForUse(entity.getAvailableForUse());

        return dto;
    }
}
