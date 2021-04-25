package com.senla.srs.service.impl;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.model.*;
import com.senla.srs.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalSessionValidatorImpl implements RentalSessionValidator {
    private final UserService userService;
    private final ScooterService scooterService;
    private final SeasonTicketService seasonTicketService;
    private final PromoCodService promoCodService;

    @Override
    public boolean isValid(RentalSessionRequestDTO rentalSessionRequestDTO) {
        Optional<User> optionalUser = userService.retrieveUserById(rentalSessionRequestDTO.getUserId());
        Optional<Scooter> optionalScooter =
                scooterService.retrieveScooterBySerialNumber(rentalSessionRequestDTO.getScooterSerialNumber());

        return isUserValid(optionalUser) &&
                isValidScooter(optionalScooter) &&
                isValidDateTime(rentalSessionRequestDTO) &&
                isValidSeasonTicket(rentalSessionRequestDTO, optionalUser, optionalScooter) &&
                isValidPromoCod(rentalSessionRequestDTO);
    }

    private boolean isValidPromoCod(RentalSessionRequestDTO rentalSessionRequestDTO) {
        Optional<PromoCod> optionalPromoCod = promoCodService.retrievePromoCodByName(rentalSessionRequestDTO.getPromoCodName());

        return optionalPromoCod.isEmpty() ||
                (optionalPromoCod.get().getAvailable() &&
                        !optionalPromoCod.get().getStartDate().isAfter(ChronoLocalDate.from(rentalSessionRequestDTO.getBeginDate())) &&
                        optionalPromoCod.get().getExpiredDate().isAfter(ChronoLocalDate.from(rentalSessionRequestDTO.getBeginDate())));
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

                !seasonTicket.getStartDate().isAfter(ChronoLocalDate.from(rentalSessionRequestDTO.getBeginDate())) &&
                seasonTicket.getExpiredDate().isAfter(ChronoLocalDate.from(rentalSessionRequestDTO.getBeginDate()));
    }

    private boolean isValidDateTime(RentalSessionRequestDTO rentalSessionRequestDTO) {
        LocalDate beginDate = rentalSessionRequestDTO.getBeginDate();
        LocalTime beginTime = rentalSessionRequestDTO.getBeginTime();

        LocalDate endDate = rentalSessionRequestDTO.getEndDate();
        LocalTime endTime = rentalSessionRequestDTO.getEndTime();

        if (endDate == null && endTime == null) {
            return true;
        } else if (endDate != null && endTime != null) {
            LocalDateTime begin = LocalDateTime.of(beginDate, beginTime);
            LocalDateTime end = LocalDateTime.of(endDate, endTime);
            return Duration.between(begin, end).getSeconds() > 0;
        } else {
            return false;
        }
    }

    private boolean isValidScooter(Optional<Scooter> optionalScooter) {
        return optionalScooter.isPresent() && optionalScooter.get().getStatus() == ScooterStatus.AVAILABLE;
    }

    private boolean isUserValid(Optional<User> optionalUser) {
        return optionalUser.isPresent() && optionalUser.get().getBalance() > 0;
    }
}
