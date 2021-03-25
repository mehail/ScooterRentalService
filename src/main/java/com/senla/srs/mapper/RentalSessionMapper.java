package com.senla.srs.mapper;

import com.senla.srs.dto.RentalSessionResponseDTO;
import com.senla.srs.model.RentalSession;

public class RentalSessionMapper extends AbstractMapper<RentalSession, RentalSessionResponseDTO> {

    public RentalSessionMapper() {
        super(RentalSession.class, RentalSessionResponseDTO.class);
    }

}
