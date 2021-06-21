package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.rentalsession.RentalSessionCompactResponseDTO;
import com.senla.srs.core.entity.RentalSession;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RentalSessionCompactResponseMapper extends AbstractMapperWithPagination<RentalSession, RentalSessionCompactResponseDTO> {

    public RentalSessionCompactResponseMapper() {
        super(RentalSession.class, RentalSessionCompactResponseDTO.class);
    }

    @Override
    public RentalSessionCompactResponseDTO toDto(RentalSession entity) {
        RentalSessionCompactResponseDTO dto = super.toDto(entity);

        dto.setScooterId(entity.getScooter().getSerialNumber());
        dto.setBegin(LocalDateTime.of(entity.getBeginDate(), entity.getBeginTime()));

        if (entity.getEndDate() != null && entity.getEndTime() != null) {
            dto.setEnd(LocalDateTime.of(entity.getEndDate(), entity.getEndTime()));
        }

        return dto;
    }

}
