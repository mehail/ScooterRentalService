package com.senla.srs.mapper;

import com.senla.srs.dto.RentalSessionRequestDTO;
import com.senla.srs.model.RentalSession;
import org.springframework.stereotype.Component;

@Component
public class RentalSessionRequestMapper extends AbstractMapper<RentalSession, RentalSessionRequestDTO> {
    public RentalSessionRequestMapper() {
        super(RentalSession.class, RentalSessionRequestDTO.class);
    }
}
