package com.senla.srs.mapper;

import com.senla.srs.dto.RentalSessionResponseDTO;
import com.senla.srs.model.RentalSession;
import org.springframework.stereotype.Component;

@Component
public class RentalSessionResponseMapper extends AbstractMapper<RentalSession, RentalSessionResponseDTO>{
    RentalSessionResponseMapper(Class<RentalSession> entityClass, Class<RentalSessionResponseDTO> dtoClass) {
        super(entityClass, dtoClass);
    }
}
