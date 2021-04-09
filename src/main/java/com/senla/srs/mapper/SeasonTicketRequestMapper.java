package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.model.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketRequestMapper extends AbstractMapper<SeasonTicket, SeasonTicketRequestDTO> {
    public SeasonTicketRequestMapper() {
        super(SeasonTicket.class, SeasonTicketRequestDTO.class);
    }

    public SeasonTicket toConsistencySeasonTicket(SeasonTicketRequestDTO seasonTicketRequestDTO, int price,
                                                  int duration) {

        SeasonTicket seasonTicket = toEntity(seasonTicketRequestDTO);

        seasonTicket.setPrice(price);
        seasonTicket.setAvailableForUse(true);
        seasonTicket.setExpiredDate(seasonTicket.getStartDate().plusDays(duration));

        return seasonTicket;
    }
}
