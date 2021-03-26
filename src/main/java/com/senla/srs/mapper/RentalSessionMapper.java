package com.senla.srs.mapper;

import com.senla.srs.dto.RentalSessionDTO;
import com.senla.srs.model.RentalSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RentalSessionMapper extends AbstractMapper<RentalSession, RentalSessionDTO> {

    @Autowired
    public RentalSessionMapper() {
        super(RentalSession.class, RentalSessionDTO.class);
    }

}
