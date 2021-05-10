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
                                  Integer rate,
                                  SeasonTicket seasonTicket,
                                  PromoCod promoCod) {
        var rentalSession = super.toEntity(dto);

        rentalSession.setBeginDate(dto.getBegin().toLocalDate());
        rentalSession.setBeginTime(dto.getBegin().toLocalTime());

        if (dto.getEnd() != null) {
            rentalSession.setEndDate(dto.getEnd().toLocalDate());
            rentalSession.setEndTime(dto.getEnd().toLocalTime());
        }

        rentalSession.setRate(rate);
        rentalSession.setUser(user);
        rentalSession.setScooter(scooter);
        rentalSession.setSeasonTicket(seasonTicket);
        rentalSession.setPromoCod(promoCod);

        return rentalSession;
    }

    public RentalSession toEntity(RentalSessionRequestDTO dto,
                                  User user,
                                  Scooter scooter,
                                  Integer rate,
                                  SeasonTicket seasonTicket,
                                  PromoCod promoCod,
                                  Long id) {

        var rentalSession = toEntity(dto, user, scooter, rate, seasonTicket, promoCod);
        rentalSession.setId(id);

        return rentalSession;
    }

}
