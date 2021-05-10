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

    public SeasonTicket toEntity(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                 ScooterType scooterType,
                                 int price,
                                 int remainingTime,
                                 int duration) {

        var seasonTicket = new SeasonTicket();

        seasonTicket.setUserId(seasonTicketRequestDTO.getUserId());
        seasonTicket.setScooterType(scooterType);
        seasonTicket.setPrice(price);
        seasonTicket.setRemainingTime(remainingTime);
        seasonTicket.setStartDate(seasonTicketRequestDTO.getStartDate());
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

        return seasonTicket;
    }

}
