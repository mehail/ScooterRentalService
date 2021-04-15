package com.senla.srs.mapper;

import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.model.SeasonTicket;
import com.senla.srs.service.ScooterTypeService;
import org.springframework.stereotype.Component;

@Component
public class SeasonTicketRequestMapper extends AbstractMapper<SeasonTicket, SeasonTicketRequestDTO> {
    private final ScooterTypeService scooterTypeService;

    public SeasonTicketRequestMapper(ScooterTypeService scooterTypeService) {
        super(SeasonTicket.class, SeasonTicketRequestDTO.class);
        this.scooterTypeService = scooterTypeService;
    }

    public SeasonTicket toConsistencySeasonTicket(SeasonTicketRequestDTO seasonTicketRequestDTO, int remainingTime,
                                                  int duration) {

        SeasonTicket seasonTicket = toEntity(seasonTicketRequestDTO);

        scooterTypeService.retrieveScooterTypeById(seasonTicketRequestDTO.getScooterTypeId()).
                ifPresent(seasonTicket::setScooterType);
        seasonTicket.setRemainingTime(remainingTime);
        seasonTicket.setExpiredDate(seasonTicket.getStartDate().plusDays(duration));
        seasonTicket.setAvailableForUse(true);

        return seasonTicket;
    }
}
