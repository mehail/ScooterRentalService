package com.senla.srs.validator.impl;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.entity.*;
import com.senla.srs.validator.RentalSessionRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RentalSessionRequestValidatorImpl implements RentalSessionRequestValidator {

    private static final String SEASON_TICKET_ID = "seasonTicketId";
    private static final String PROMO_COD_NAME = "promoCodName";

    @Override
    public RentalSessionRequestDTO validate(RentalSessionRequestDTO rentalSessionRequestDTO,
                                            Optional<RentalSession> optionalRentalSession,
                                            Optional<User> optionalUser,
                                            Optional<Scooter> optionalScooter,
                                            Optional<SeasonTicket> optionalSeasonTicket,
                                            Optional<PromoCod> optionalPromoCod,
                                            Errors errors) {

        return optionalRentalSession.isEmpty()
                ? validateNewEntity(rentalSessionRequestDTO,
                optionalUser,
                optionalScooter,
                optionalSeasonTicket,
                optionalPromoCod,
                errors)

                : validateExistEntity(rentalSessionRequestDTO,
                optionalRentalSession,
                errors);
    }

    private RentalSessionRequestDTO validateNewEntity(RentalSessionRequestDTO rentalSessionRequestDTO,
                                                      Optional<User> optionalUser,
                                                      Optional<Scooter> optionalScooter,
                                                      Optional<SeasonTicket> optionalSeasonTicket,
                                                      Optional<PromoCod> optionalPromoCod,
                                                      Errors errors) {

        validateUser(optionalUser, errors);
        validateScooter(optionalScooter, errors);
        validateSeasonTicket(rentalSessionRequestDTO, optionalSeasonTicket, optionalScooter, errors);
        validatePromoCod(rentalSessionRequestDTO, optionalPromoCod, errors);
        validateBeginAndEnd(rentalSessionRequestDTO.getBegin(), rentalSessionRequestDTO.getEnd(), errors);

        return rentalSessionRequestDTO;
    }

    private void validateBeginAndEnd(LocalDateTime begin, LocalDateTime end, Errors errors) {

        if (end != null && !end.isAfter(begin)) {
            errors.reject("end", "The session end time must be later than the begin");
        }

    }

    private void validatePromoCod(RentalSessionRequestDTO rentalSessionRequestDTO,
                                  Optional<PromoCod> optionalPromoCod,
                                  Errors errors) {

        if (rentalSessionRequestDTO.getPromoCodName() != null) {

            if (optionalPromoCod.isEmpty()) {
                errors.reject(PROMO_COD_NAME, "PromoCod with this name does not exist");
            } else {
                var promoCod = optionalPromoCod.get();

                if (!promoCod.getAvailable()) {
                    errors.reject(PROMO_COD_NAME, "PromoCod with this name not available for use");
                }

                var sessionBegin = rentalSessionRequestDTO.getBegin().toLocalDate();
                LocalDate promoCodStart = promoCod.getStartDate();
                LocalDate promoCodExpired = promoCod.getExpiredDate();

                if (!sessionBegin.isBefore(promoCodStart) && sessionBegin.isBefore(promoCodExpired)) {
                    errors.reject(PROMO_COD_NAME, "The start of the rental session does not match the PromoCod");
                }

            }

        }

    }

    private void validateSeasonTicket(RentalSessionRequestDTO rentalSessionRequestDTO,
                                      Optional<SeasonTicket> optionalSeasonTicket,
                                      Optional<Scooter> optionalScooter,
                                      Errors errors) {

        if (rentalSessionRequestDTO.getSeasonTicketId() != null) {

            if (optionalSeasonTicket.isEmpty()) {
                errors.reject(SEASON_TICKET_ID, "Season ticket with this ID does not exist");
            } else {
                var seasonTicket = optionalSeasonTicket.get();

                if (!seasonTicket.getAvailableForUse()) {
                    errors.reject(SEASON_TICKET_ID, "Season ticket with this ID not available for use");
                }

                var sessionBegin = rentalSessionRequestDTO.getBegin().toLocalDate();
                LocalDate seasonStart = seasonTicket.getStartDate();
                LocalDate seasonExpired = seasonTicket.getExpiredDate();

                if (sessionBegin.isBefore(seasonStart) && !sessionBegin.isBefore(seasonExpired)) {
                    errors.reject(SEASON_TICKET_ID, "The start of the rental session does not match the season ticket");
                }

                if (optionalScooter.isEmpty() || optionalScooter.get().getType() != seasonTicket.getScooterType()) {
                    errors.reject(SEASON_TICKET_ID, "Season ticket with this ID does not match the type of scooter");
                }

            }

        }

    }

    private void validateScooter(Optional<Scooter> optionalScooter, Errors errors) {

        if (optionalScooter.isEmpty()) {
            errors.reject("scooterSerialNumber", "Scooter with this serial number does not exist");
        } else if (optionalScooter.get().getStatus() != ScooterStatus.AVAILABLE) {
            errors.reject("scooterSerialNumber", "Scooter with this serial number not available for use");
        }

    }

    private void validateUser(Optional<User> optionalUser, Errors errors) {

        if (optionalUser.isEmpty()) {
            errors.reject("userId", "User with this ID does not exist");
        } else if (optionalUser.get().getBalance() <= 0) {
            errors.reject("userId", "User balance must be greater than 0");
        }

    }

    private RentalSessionRequestDTO validateExistEntity(RentalSessionRequestDTO rentalSessionRequestDTO,
                                                        Optional<RentalSession> optionalExistRentalSession,
                                                        Errors errors) {

        if (optionalExistRentalSession.isPresent()) {
            var existRentalSession = optionalExistRentalSession.get();

            if (existRentalSession.getEndDate() != null || existRentalSession.getEndTime() != null) {
                errors.reject("this", "Completed rental session is not available for editing");
            } else {
                validateMatchExistRentalSession(rentalSessionRequestDTO, existRentalSession, errors);

                validateBeginAndEnd(LocalDateTime.of(existRentalSession.getBeginDate(), existRentalSession.getBeginTime()),
                        rentalSessionRequestDTO.getEnd(), errors);
            }

        }

        return rentalSessionRequestDTO;
    }

    private void validateMatchExistRentalSession(RentalSessionRequestDTO rentalSessionRequestDTO,
                                                 RentalSession existRentalSession,
                                                 Errors errors) {

        boolean isMatchUserId = rentalSessionRequestDTO.getUserId().equals(existRentalSession.getUser().getId());
        boolean isMatchBeginDate =
                rentalSessionRequestDTO.getBegin().toLocalDate().equals(existRentalSession.getBeginDate());
        boolean isMatchBeginTime =
                rentalSessionRequestDTO.getBegin().toLocalTime().equals(existRentalSession.getBeginTime());
        boolean isMatchScooterSerialNumber =
                rentalSessionRequestDTO.getScooterSerialNumber().equals(existRentalSession.getScooter().getSerialNumber());

        boolean resultMatch = isMatchUserId
                && isMatchBeginDate
                && isMatchBeginTime
                && isMatchScooterSerialNumber
                && isMatchSeasonTicketId(rentalSessionRequestDTO, existRentalSession)
                && isMatchPromoCodName(rentalSessionRequestDTO, existRentalSession);

        if (!resultMatch) {
            errors.reject("this", "In an open rental session, only the end can be changed");
        }

    }

    private boolean isMatchPromoCodName(RentalSessionRequestDTO rentalSessionRequestDTO, RentalSession existRentalSession) {
        String dtoPromoCodName = rentalSessionRequestDTO.getPromoCodName();
        var existPromoCod = existRentalSession.getPromoCod();

        if (dtoPromoCodName == null && existPromoCod == null) {
            return true;
        } else if (dtoPromoCodName != null && existPromoCod != null) {
            return dtoPromoCodName.equals(existPromoCod.getName());
        }

        return false;
    }

    private boolean isMatchSeasonTicketId(RentalSessionRequestDTO rentalSessionRequestDTO, RentalSession existRentalSession) {
        Long dtoSeasonTicketId = rentalSessionRequestDTO.getSeasonTicketId();
        var existSeasonTicket = existRentalSession.getSeasonTicket();

        if (dtoSeasonTicketId == null && existSeasonTicket == null) {
            return true;
        } else if (dtoSeasonTicketId != null && existSeasonTicket != null) {
            return dtoSeasonTicketId.equals(existSeasonTicket.getId());
        }

        return false;
    }

}
