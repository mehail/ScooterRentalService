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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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
                    .orElseThrow(() -> new NotFoundEntityException(RentalSession.class, id)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized user session requested", HttpStatus.FORBIDDEN);
        }

    }

    @Transactional
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

            Optional<SeasonTicket> optionalSeasonTicket = rentalSessionRequestDTO.getSeasonTicketId() != null
                    ? seasonTicketService.retrieveSeasonTicketsById(rentalSessionRequestDTO.getSeasonTicketId())
                    : Optional.empty();

            Optional<PromoCod> optionalPromoCod = rentalSessionRequestDTO.getPromoCodName() != null
                    ? promoCodService.retrievePromoCodByName(rentalSessionRequestDTO.getPromoCodName())
                    : Optional.empty();

            RentalSessionRequestDTO validRentalSessionDTO = rentalSessionRequestValidator.validate(rentalSessionRequestDTO,
                    optionalRentalSession,
                    optionalUser,
                    optionalScooter,
                    optionalSeasonTicket,
                    optionalPromoCod,
                    bindingResult);

            return save(validRentalSessionDTO, optionalUser, optionalScooter, optionalSeasonTicket, optionalPromoCod, bindingResult);
        } else {
            return new ResponseEntity<>("Change someone else's rental session is not available", HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public ResponseEntity<?> delete(Long id, String token) throws NotFoundEntityException {
        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (optionalRentalSession.map(RentalSession::getEndDate).isEmpty()) {

            rentalSessionService.deleteById(id);
            return new ResponseEntity<>("Rental session with this id was deleted", HttpStatus.ACCEPTED);

        } else {
            return new ResponseEntity<>("Rental session closed and cannot be deleted", HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public Long getExistEntityId(RentalSessionRequestDTO dto) {
        Optional<RentalSession> optionalExistRentalSession =
                rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(dto.getUserId(),
                        dto.getScooterSerialNumber(), dto.getBegin().toLocalDate(), dto.getBegin().toLocalTime());

        return optionalExistRentalSession
                .map(RentalSession::getId)
                .orElse(null);
    }

    private ResponseEntity<?> save(RentalSessionRequestDTO rentalSessionRequestDTO,
                                   Optional<User> optionalUser,
                                   Optional<Scooter> optionalScooter,
                                   Optional<SeasonTicket> optionalSeasonTicket,
                                   Optional<PromoCod> optionalPromoCod,
                                   BindingResult bindingResult)
            throws NotFoundEntityException {

        if (!bindingResult.hasErrors()) {
            var rentalSession = toEntity(rentalSessionRequestDTO,
                    optionalUser,
                    optionalScooter,
                    optionalSeasonTicket,
                    optionalPromoCod);

            rentalSession.setRate(calculateRate(rentalSession));

            var savedRentalSession = rentalSessionService.save(rentalSession);

            changeEntityState(rentalSession, savedRentalSession.getRate());

            return new ResponseEntity<>(rentalSessionResponseMapper.toDto(savedRentalSession), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }

    private RentalSession toEntity(RentalSessionRequestDTO rentalSessionRequestDTO,
                                   Optional<User> optionalUser,
                                   Optional<Scooter> optionalScooter,
                                   Optional<SeasonTicket> optionalSeasonTicket,
                                   Optional<PromoCod> optionalPromoCod)
            throws NotFoundEntityException {

        var user = optionalUser.orElseThrow(() -> new NotFoundEntityException(User.class));

        var scooter = optionalScooter.orElseThrow(() -> new NotFoundEntityException(Scooter.class));

        Long seasonTicketId = rentalSessionRequestDTO.getSeasonTicketId();
        SeasonTicket seasonTicket = seasonTicketId != null
                ? optionalSeasonTicket.orElseThrow(() -> new NotFoundEntityException(SeasonTicket.class, seasonTicketId))
                : null;

        String promoCodName = rentalSessionRequestDTO.getPromoCodName();
        PromoCod promoCod = promoCodName != null
                ? optionalPromoCod.orElseThrow(() -> new NotFoundEntityException(PromoCod.class, promoCodName))
                : null;

        return rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO, user, scooter, 0, seasonTicket,
                promoCod, getExistEntityId(rentalSessionRequestDTO));
    }

    private boolean isThisUserRentalSession(Optional<RentalSession> optionalRentalSession,
                                            String token) {

        return optionalRentalSession.isPresent() &&
                isThisUserById(token, optionalRentalSession.get().getUser().getId());
    }

    private void changeEntityState(RentalSession rentalSession, Integer rate) {
        var scooter = rentalSession.getScooter();
        var seasonTicket = rentalSession.getSeasonTicket();
        var user = rentalSession.getUser();

        if (rentalSession.getEndDate() == null) {
            scooter.setStatus(ScooterStatus.USED);

            if (seasonTicket != null) {
                seasonTicket.setAvailableForUse(false);
            }

        } else {
            user.setBalance(user.getBalance() - rate);

            scooter.setStatus(ScooterStatus.AVAILABLE);

            scooter.setTimeMillage(scooter.getTimeMillage() + getUsageTime(rentalSession));

            if (seasonTicket != null && seasonTicket.getRemainingTime() > 0) {
                seasonTicket.setAvailableForUse(true);
            }

        }

    }

    private int calculateRate(RentalSession rentalSession) {
        int pricePerMinute = rentalSession.getScooter().getType().getPricePerMinute();
        int billableTime = calculateBillableTime(rentalSession.getSeasonTicket(), getUsageTime(rentalSession));
        int priceWithoutPromoCod = billableTime * pricePerMinute;

        return applyPromoCod(priceWithoutPromoCod, rentalSession.getPromoCod(), rentalSession.getUser());
    }

    private int getUsageTime(RentalSession rentalSession) {

        if (rentalSession.getEndDate() != null && rentalSession.getEndTime() != null) {
            var begin = LocalDateTime.of(rentalSession.getBeginDate(), rentalSession.getBeginTime());
            var end = LocalDateTime.of(rentalSession.getEndDate(), rentalSession.getEndTime());
            var usageTime = (int) (Duration.between(begin, end).getSeconds() / 60);

            return usageTime == 0 ? 1 : usageTime;
        } else {
            return 0;
        }

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
        var discountPercentage = 0;

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
