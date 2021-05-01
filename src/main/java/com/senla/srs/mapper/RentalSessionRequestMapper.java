package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.entity.*;
import org.springframework.stereotype.Component;

@Component
public class RentalSessionRequestMapper extends AbstractMapper<RentalSession, RentalSessionRequestDTO> {
    public RentalSessionRequestMapper() {
        super(RentalSession.class, RentalSessionRequestDTO.class);
    }

    public RentalSession toEntity(RentalSessionRequestDTO dto,
                                  User user,
                                  Scooter scooter,
                                  SeasonTicket seasonTicket,
                                  PromoCod promoCod) {
        RentalSession rentalSession = super.toEntity(dto);

        rentalSession.setUser(user);
        rentalSession.setScooter(scooter);
        rentalSession.setSeasonTicket(seasonTicket);
        rentalSession.setPromoCod(promoCod);

        return rentalSession;
    }
}
