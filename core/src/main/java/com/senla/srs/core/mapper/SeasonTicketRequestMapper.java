package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.core.entity.ScooterType;
import com.senla.srs.core.entity.SeasonTicket;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketRequestMapper extends AbstractMapper<SeasonTicket, SeasonTicketRequestDTO> {

    public SeasonTicketRequestMapper() {
        super(SeasonTicket.class, SeasonTicketRequestDTO.class);
    }

    public SeasonTicket toEntity(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                 ScooterType scooterType,
                                 int price,
                                 int remainingTime,
                                 int duration) {

        var seasonTicket = super.toEntity(seasonTicketRequestDTO);

        seasonTicket.setScooterType(scooterType);
        seasonTicket.setPrice(price);
        seasonTicket.setRemainingTime(remainingTime);
        seasonTicket.setExpiredDate(seasonTicket.getStartDate().plusDays(duration));
        seasonTicket.setAvailableForUse(true);

        return seasonTicket;
    }

    public SeasonTicket toEntity(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                 ScooterType scooterType,
                                 int price,
                                 int remainingTime,
                                 int duration,
                                 Long id) {

        var seasonTicket = toEntity(seasonTicketRequestDTO, scooterType, price, remainingTime, duration);
        seasonTicket.setId(id);

        return null;
    }

}
