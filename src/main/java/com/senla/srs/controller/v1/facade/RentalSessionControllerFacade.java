package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.rentalsession.RentalSessionDTO;
import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.entity.*;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.RentalSessionRequestMapper;
import com.senla.srs.mapper.RentalSessionResponseMapper;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.*;
import com.senla.srs.validator.RentalSessionRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
public class RentalSessionControllerFacade extends AbstractFacade implements
        EntityControllerFacade<RentalSessionDTO, RentalSessionRequestDTO, RentalSessionResponseDTO, Long> {

    private final PromoCodService promoCodService;
    private final RentalSessionService rentalSessionService;
    private final RentalSessionRequestMapper rentalSessionRequestMapper;
    private final RentalSessionResponseMapper rentalSessionResponseMapper;
    private final ScooterService scooterService;
    private final SeasonTicketService seasonTicketService;
    private final UserService userService;
    private final RentalSessionRequestValidator rentalSessionRequestValidator;

    public RentalSessionControllerFacade(PromoCodService promoCodService,
                                         RentalSessionService rentalSessionService,
                                         RentalSessionRequestMapper rentalSessionRequestMapper,
                                         RentalSessionResponseMapper rentalSessionResponseMapper,
                                         ScooterService scooterService,
                                         SeasonTicketService seasonTicketService,
                                         RentalSessionRequestValidator rentalSessionRequestValidator,
                                         UserService userService,
                                         JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.rentalSessionService = rentalSessionService;
        this.rentalSessionRequestMapper = rentalSessionRequestMapper;
        this.rentalSessionResponseMapper = rentalSessionResponseMapper;
        this.scooterService = scooterService;
        this.seasonTicketService = seasonTicketService;
        this.promoCodService = promoCodService;
        this.userService = userService;
        this.rentalSessionRequestValidator = rentalSessionRequestValidator;
    }

    @Override
    public Page<RentalSessionResponseDTO> getAll(Integer page, Integer size, String sort, String token) {
        return isAdmin(token)
                ? rentalSessionResponseMapper.mapPageToDtoPage(rentalSessionService.retrieveAllRentalSessions(page, size, sort))

                : rentalSessionResponseMapper.mapPageToDtoPage(
                rentalSessionService.retrieveAllRentalSessionsByUserId(getAuthUserId(token), page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, String token) throws NotFoundEntityException {
        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (isAdmin(token) || isThisUserRentalSession(optionalRentalSession, token)) {
            return new ResponseEntity<>(optionalRentalSession
                    .map(rentalSessionResponseMapper::toDto)
                    .orElseThrow(() -> new NotFoundEntityException("Rental session")), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized user session requested", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<?> createOrUpdate(RentalSessionRequestDTO rentalSessionRequestDTO,
                                            BindingResult bindingResult,
                                            String token)
            throws NotFoundEntityException {


        if (isAdmin(token) || isThisUserById(token, rentalSessionRequestDTO.getUserId())) {

            Optional<RentalSession> optionalRentalSession =
                    rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(
                            rentalSessionRequestDTO.getUserId(),
                            rentalSessionRequestDTO.getScooterSerialNumber(),
                            rentalSessionRequestDTO.getBegin().toLocalDate(),
                            rentalSessionRequestDTO.getBegin().toLocalTime());
            Optional<User> optionalUser = userService.retrieveUserById(rentalSessionRequestDTO.getUserId());
            Optional<Scooter> optionalScooter =
                    scooterService.retrieveScooterBySerialNumber(rentalSessionRequestDTO.getScooterSerialNumber());
            Optional<SeasonTicket> optionalSeasonTicket =
                    seasonTicketService.retrieveSeasonTicketsById(rentalSessionRequestDTO.getSeasonTicketId());
            Optional<PromoCod> optionalPromoCod =
                    promoCodService.retrievePromoCodByName(rentalSessionRequestDTO.getPromoCodName());

            RentalSessionRequestDTO validRentalSessionDTO = rentalSessionRequestValidator.validate(rentalSessionRequestDTO,
                    optionalRentalSession,
                    optionalUser,
                    optionalScooter,
                    optionalSeasonTicket,
                    optionalPromoCod,
                    bindingResult);

            RentalSession rentalSession =
                    toEntity(validRentalSessionDTO, optionalUser,optionalScooter,optionalSeasonTicket, optionalPromoCod);

            int rate = calculateRate(rentalSession);
            rentalSession.setRate(rate);

            changeEntityState(rentalSession, rate);

            return save(rentalSession, bindingResult);
        } else {
            return new ResponseEntity<>("Change someone else's rental session is not available", HttpStatus.FORBIDDEN);
        }

    }

    private RentalSession toEntity(RentalSessionRequestDTO rentalSessionRequestDTO,
                                   Optional<User> optionalUser,
                                   Optional<Scooter> optionalScooter,
                                   Optional<SeasonTicket> optionalSeasonTicket,
                                   Optional<PromoCod> optionalPromoCod)
            throws NotFoundEntityException {

        User user = optionalUser.orElseThrow(() -> new NotFoundEntityException("User"));

        Scooter scooter = optionalScooter.orElseThrow(() -> new NotFoundEntityException("Scooter"));

        SeasonTicket seasonTicket = rentalSessionRequestDTO.getSeasonTicketId() != null
                ? optionalSeasonTicket.orElseThrow(() -> new NotFoundEntityException("Season"))
                : null;

        PromoCod promoCod = rentalSessionRequestDTO.getPromoCodName() != null
                ? optionalPromoCod.orElseThrow(() -> new NotFoundEntityException("PromoCod"))
                : null;

        return rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO, user, scooter, 0, seasonTicket, promoCod);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (optionalRentalSession.isPresent()
                && optionalRentalSession.get().getEndDate() == null
        ) {
            rentalSessionService.deleteById(id);
            return new ResponseEntity<>("Rental session with this id was deleted", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Rental session closed and cannot be deleted", HttpStatus.FORBIDDEN);
        }
    }

    private boolean isThisUserRentalSession(Optional<RentalSession> optionalRentalSession,
                                            String token) {

        return optionalRentalSession.isPresent() &&
                isThisUserById(token, optionalRentalSession.get().getUser().getId());
    }

    private ResponseEntity<?> save(RentalSession rentalSession,
                                   BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok(rentalSessionResponseMapper.toDto(rentalSessionService.save(rentalSession)));
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
    }

    private void changeEntityState(RentalSession rentalSession, Integer rate) {
        Scooter scooter = rentalSession.getScooter();
        SeasonTicket seasonTicket = rentalSession.getSeasonTicket();
        User user = rentalSession.getUser();

        if (rentalSession.getEndDate() == null) {
            scooter.setStatus(ScooterStatus.USED);

            if (seasonTicket != null) {
                seasonTicket.setAvailableForUse(false);
            }

        } else {
            user.setBalance(user.getBalance() - rate);

            scooter.setStatus(ScooterStatus.AVAILABLE);

            int usageTime = (int) (Duration.between(rentalSession.getBeginDate(), rentalSession.getEndDate()).getSeconds() / 60);
            scooter.setTimeMillage(scooter.getTimeMillage() + usageTime);

            if (seasonTicket != null && seasonTicket.getRemainingTime() > 0) {
                seasonTicket.setAvailableForUse(true);
            }

        }
    }

    private int calculateRate(RentalSession rentalSession) {
        LocalDateTime begin = LocalDateTime.of(rentalSession.getBeginDate(), rentalSession.getBeginTime());
        LocalDateTime end = LocalDateTime.of(rentalSession.getEndDate(), rentalSession.getBeginTime());
        int usageTime = (int) (Duration.between(begin, end).getSeconds() / 60);

        int pricePerMinute = rentalSession.getScooter().getType().getPricePerMinute();

        int billableTime = calculateBillableTime(rentalSession.getSeasonTicket(), usageTime);

        int priceWithoutPromoCod = billableTime * pricePerMinute;

        return applyPromoCod(priceWithoutPromoCod, rentalSession.getPromoCod(), rentalSession.getUser());
    }

    private int calculateBillableTime(SeasonTicket seasonTicket, int usageTime) {
        if (seasonTicket != null) {

            if (seasonTicket.getRemainingTime() >= usageTime) {
                seasonTicket.setRemainingTime(seasonTicket.getRemainingTime() - usageTime);
                usageTime = 0;
            } else {
                usageTime -= seasonTicket.getRemainingTime();
                seasonTicket.setRemainingTime(0);
            }

            if (seasonTicket.getRemainingTime() == 0) {
                seasonTicket.setAvailableForUse(false);
            }

        }

        return usageTime;
    }

    private int applyPromoCod(int rate, PromoCod promoCod, User user) {
        int discountPercentage = 0;

        if (promoCod != null) {

            if (promoCod.getBonusPoint() > 0) {
                user.setBalance(user.getBalance() + promoCod.getBonusPoint());
            }

            if (promoCod.getDiscountPercentage() > 0) {
                discountPercentage = promoCod.getDiscountPercentage();
            }

            promoCod.setAvailable(false);
        }

        return rate * (100 - discountPercentage) / 100;
    }
}
