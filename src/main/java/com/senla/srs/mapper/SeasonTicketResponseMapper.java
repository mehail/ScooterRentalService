package com.senla.srs.mapper;

import com.senla.srs.dto.SeasonTicketResponseDTO;
import com.senla.srs.model.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketResponseMapper extends AbstractMapper<SeasonTicket, SeasonTicketResponseDTO>{
    SeasonTicketResponseMapper(Class<SeasonTicket> entityClass, Class<SeasonTicketResponseDTO> dtoClass) {
        super(entityClass, dtoClass);
    }
}
