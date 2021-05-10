package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.entity.RentalSession;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RentalSessionResponseMapper extends AbstractMapperWithPagination<RentalSession, RentalSessionResponseDTO>{

    public RentalSessionResponseMapper() {
        super(RentalSession.class, RentalSessionResponseDTO.class);
    }

    @Override
    public RentalSessionResponseDTO toDto(RentalSession entity) {
        RentalSessionResponseDTO dto =  super.toDto(entity);

        dto.setBegin(LocalDateTime.of(entity.getBeginDate(), entity.getBeginTime()));

        if (entity.getEndDate() != null && entity.getEndTime() != null) {
            dto.setEnd(LocalDateTime.of(entity.getEndDate(), entity.getEndTime()));
        }

        return dto;
    }

}
