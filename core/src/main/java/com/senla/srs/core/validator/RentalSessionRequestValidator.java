package com.senla.srs.core.validator;

import com.senla.srs.core.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.core.entity.*;
import com.senla.srs.core.service.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service("rentalSessionRequestValidator")
public class RentalSessionRequestValidator implements Validator {

    private static final String SEASON_TICKET_ID = "seasonTicketId";
    private static final String PROMO_COD_NAME = "promoCodName";
    private final RentalSessionService rentalSessionService;
    private final UserService userService;
    private final ScooterService scooterService;
    private final SeasonTicketService seasonTicketService;
    private final PromoCodService promoCodService;

    public RentalSessionRequestValidator(RentalSessionService rentalSessionService, UserService userService, ScooterService scooterService, SeasonTicketService seasonTicketService, PromoCodService promoCodService) {
        this.rentalSessionService = rentalSessionService;
        this.userService = userService;
        this.scooterService = scooterService;
        this.seasonTicketService = seasonTicketService;
        this.promoCodService = promoCodService;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return RentalSessionRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {

        RentalSessionRequestDTO rentalSessionRequestDTO = (RentalSessionRequestDTO) o;

        Optional<RentalSession> optionalRentalSession =
                rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(
                        rentalSessionRequestDTO.getUserId(),
                        rentalSessionRequestDTO.getScooterSerialNumber(),
                        rentalSessionRequestDTO.getBegin().toLocalDate(),
                        rentalSessionRequestDTO.getBegin().toLocalTime());

        Optional<User> optionalUser = userService.retrieveUserById(rentalSessionRequestDTO.getUserId());

        Optional<Scooter> optionalScooter =
                scooterService.retrieveScooterBySerialNumber(rentalSessionRequestDTO.getScooterSerialNumber());

        Optional<SeasonTicket> optionalSeasonTicket = rentalSessionRequestDTO.getSeasonTicketId() != null
                ? seasonTicketService.retrieveSeasonTicketsById(rentalSessionRequestDTO.getSeasonTicketId())
                : Optional.empty();

        Optional<PromoCod> optionalPromoCod = rentalSessionRequestDTO.getPromoCodName() != null
                ? promoCodService.retrievePromoCodByName(rentalSessionRequestDTO.getPromoCodName())
                : Optional.empty();

        if (optionalRentalSession.isEmpty()) {
            validateNewEntity(rentalSessionRequestDTO,
                    optionalUser,
                    optionalScooter,
                    optionalSeasonTicket,
                    optionalPromoCod,
                    errors);
        } else {
            validateExistEntity(rentalSessionRequestDTO,
                    optionalRentalSession,
                    errors);
        }
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

                if (sessionBegin.isBefore(seasonStart) || !sessionBegin.isBefore(seasonExpired)) {
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
                rentalSessionRequestDTO.getBegin().toLocalTime().withNano(0).equals(existRentalSession.getBeginTime());
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
