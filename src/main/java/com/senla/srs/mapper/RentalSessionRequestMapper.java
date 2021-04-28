package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.model.*;
import com.senla.srs.service.PromoCodService;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.SeasonTicketService;
import com.senla.srs.service.UserService;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RentalSessionRequestMapper extends AbstractMapper<RentalSession, RentalSessionRequestDTO> {
    public RentalSessionRequestMapper() {
        super(RentalSession.class, RentalSessionRequestDTO.class);
    }

    //ToDo Refactor to builder
    public RentalSession toEntity(RentalSessionRequestDTO dto, User user, Scooter scooter, SeasonTicket seasonTicket, PromoCod promoCod) {
        RentalSession rentalSession = super.toEntity(dto);

        rentalSession.setUser(user);
        rentalSession.setScooter(scooter);
        rentalSession.setSeasonTicket(seasonTicket);
        rentalSession.setPromoCod(promoCod);

        return rentalSession;
    }
}
