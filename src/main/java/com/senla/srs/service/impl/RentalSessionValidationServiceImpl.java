package com.senla.srs.service.impl;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.model.*;
import com.senla.srs.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalSessionValidationServiceImpl implements RentalSessionValidationService {
    private final UserService userService;
    private final ScooterService scooterService;
    private final SeasonTicketService seasonTicketService;
    private final PromoCodService promoCodService;

    @Override
    public boolean isValidRentalSession(RentalSessionRequestDTO rentalSessionRequestDTO) {
        Optional<User> optionalUser = userService.retrieveUserById(rentalSessionRequestDTO.getUserId());
        Optional<Scooter> optionalScooter =
                scooterService.retrieveScooterBySerialNumber(rentalSessionRequestDTO.getScooterSerialNumber());

        return isUserValid(optionalUser) &&
                isValidScooter(optionalScooter) &&
                isValidDate(rentalSessionRequestDTO.getBegin(), rentalSessionRequestDTO.getEnd()) &&
                isValidSeasonTicket(rentalSessionRequestDTO, optionalUser, optionalScooter) &&
                isValidPromoCod(rentalSessionRequestDTO);
    }

    private boolean isValidPromoCod(RentalSessionRequestDTO rentalSessionRequestDTO) {
        Optional<PromoCod> optionalPromoCod = promoCodService.retrievePromoCodByName(rentalSessionRequestDTO.getPromoCodName());

        return optionalPromoCod.isEmpty() ||

                (optionalPromoCod.get().getAvailable() &&
                        !optionalPromoCod.get().getStartDate().
                                isAfter(ChronoLocalDate.from(rentalSessionRequestDTO.getBegin())) &&
                        optionalPromoCod.get().getExpiredDate().
                                isAfter(ChronoLocalDate.from(rentalSessionRequestDTO.getBegin())));
    }

    private boolean isValidSeasonTicket(RentalSessionRequestDTO rentalSessionRequestDTO,
                                        Optional<User> optionalUser,
                                        Optional<Scooter> optionalScooter) {

        Optional<SeasonTicket> optionalSeasonTicket =
                seasonTicketService.retrieveSeasonTicketsById(rentalSessionRequestDTO.getSeasonTicketId());

        if (optionalSeasonTicket.isEmpty()) {
            return true;
        }

        SeasonTicket seasonTicket = optionalSeasonTicket.get();

        return seasonTicket.getAvailableForUse() &&

                seasonTicket.getRemainingTime() > 0 &&

                (optionalUser.isPresent() && seasonTicket.getUserId().equals(optionalUser.get().getId())) &&

                (optionalScooter.isPresent() &&
                        seasonTicket.getScooterType().getId().equals(optionalScooter.get().getType().getId())) &&

                !seasonTicket.getStartDate().isAfter(ChronoLocalDate.from(rentalSessionRequestDTO.getBegin())) &&
                seasonTicket.getExpiredDate().isAfter(ChronoLocalDate.from(rentalSessionRequestDTO.getBegin()));
    }

    private boolean isValidDate(LocalDateTime begin, LocalDateTime end) {
        return end == null || begin.isBefore(end);
    }

    private boolean isValidScooter(Optional<Scooter> optionalScooter) {
        return optionalScooter.isPresent() && optionalScooter.get().getStatus() == ScooterStatus.AVAILABLE;
    }

    private boolean isUserValid(Optional<User> optionalUser) {
        return optionalUser.isPresent() && optionalUser.get().getBalance() > 0;
    }
}
