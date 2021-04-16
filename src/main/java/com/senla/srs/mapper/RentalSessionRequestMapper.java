package com.senla.srs.mapper;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.model.RentalSession;
import com.senla.srs.service.PromoCodService;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.SeasonTicketService;
import com.senla.srs.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class RentalSessionRequestMapper extends AbstractMapper<RentalSession, RentalSessionRequestDTO> {
    private final UserService userService;
    private final ScooterService scooterService;
    private final SeasonTicketService seasonTicketService;
    private final PromoCodService promoCodService;

    public RentalSessionRequestMapper(UserService userService,
                                      ScooterService scooterService,
                                      SeasonTicketService seasonTicketService,
                                      PromoCodService promoCodService) {
        super(RentalSession.class, RentalSessionRequestDTO.class);
        this.userService = userService;
        this.scooterService = scooterService;
        this.seasonTicketService = seasonTicketService;
        this.promoCodService = promoCodService;
    }

    @Override
    public RentalSession toEntity(RentalSessionRequestDTO dto) {
        RentalSession rentalSession = super.toEntity(dto);
        userService.retrieveUserById(dto.getUserId()).ifPresent(rentalSession::setUser);
        scooterService.retrieveScooterBySerialNumber(dto.getScooterSerialNumber()).ifPresent(rentalSession::setScooter);

        seasonTicketService.retrieveSeasonTicketsById(dto.getSeasonTicketId()).ifPresent(rentalSession::setSeasonTicket);
        promoCodService.retrievePromoCodByName(dto.getPromoCodName()).ifPresent(rentalSession::setPromoCod);

        return rentalSession;
    }
}
