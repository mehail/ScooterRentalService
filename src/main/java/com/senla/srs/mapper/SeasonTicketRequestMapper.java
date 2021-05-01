package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.entity.ScooterType;
import com.senla.srs.entity.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketRequestMapper extends AbstractMapper<SeasonTicket, SeasonTicketRequestDTO> {
    public SeasonTicketRequestMapper() {
        super(SeasonTicket.class, SeasonTicketRequestDTO.class);
    }

    public SeasonTicket toConsistencySeasonTicket(SeasonTicketRequestDTO seasonTicketRequestDTO, ScooterType scooterType, int remainingTime,
                                                  int duration) {

        SeasonTicket seasonTicket = toEntity(seasonTicketRequestDTO);

        seasonTicket.setScooterType(scooterType);
        seasonTicket.setRemainingTime(remainingTime);
        seasonTicket.setExpiredDate(seasonTicket.getStartDate().plusDays(duration));
        seasonTicket.setAvailableForUse(true);

        return seasonTicket;
    }
}
