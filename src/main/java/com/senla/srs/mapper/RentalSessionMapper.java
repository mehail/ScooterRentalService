package com.senla.srs.mapper;

import com.senla.srs.dto.RentalSessionDTO;
import com.senla.srs.model.RentalSession;

public class RentalSessionMapper extends AbstractMapper<RentalSession, RentalSessionDTO> {

    public RentalSessionMapper() {
        super(RentalSession.class, RentalSessionDTO.class);
    }

}
