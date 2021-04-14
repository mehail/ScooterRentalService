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

    public SeasonTicket toConsistencySeasonTicket(SeasonTicketRequestDTO seasonTicketRequestDTO, int price,
                                                  int duration) {

        SeasonTicket seasonTicket = toEntity(seasonTicketRequestDTO);

        seasonTicket.setScooterType(scooterTypeService.retrieveScooterTypeById(seasonTicketRequestDTO.getScooterTypeId()).get());
        seasonTicket.setPrice(price);
        seasonTicket.setAvailableForUse(true);
        seasonTicket.setExpiredDate(seasonTicket.getStartDate().plusDays(duration));

        return seasonTicket;
    }
}
