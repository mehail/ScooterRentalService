package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.model.RentalSession;
import org.springframework.stereotype.Component;

@Component
public class RentalSessionResponseMapper extends AbstractMapperWithPagination<RentalSession, RentalSessionResponseDTO>{
    public RentalSessionResponseMapper() {
        super(RentalSession.class, RentalSessionResponseDTO.class);
    }
}
