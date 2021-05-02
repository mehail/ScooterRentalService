package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.rentalsession.RentalSessionDTO;
import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.RentalSessionRequestMapper;
import com.senla.srs.mapper.RentalSessionResponseMapper;
import com.senla.srs.entity.*;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@Controller
public class RentalSessionControllerFacade extends AbstractFacade implements
        EntityControllerFacade<RentalSessionDTO, RentalSessionRequestDTO, RentalSessionResponseDTO, Long> {

    private final PromoCodService promoCodService;
    private final RentalSessionService rentalSessionService;
    private final RentalSessionValidator rentalSessionValidator;
    private final RentalSessionRequestMapper rentalSessionRequestMapper;
    private final RentalSessionResponseMapper rentalSessionResponseMapper;
    private final ScooterService scooterService;
    private final SeasonTicketService seasonTicketService;
    private final UserService userService;

    public RentalSessionControllerFacade(PromoCodService promoCodService,
                                         RentalSessionService rentalSessionService,
                                         RentalSessionValidator rentalSessionValidator,
                                         RentalSessionRequestMapper rentalSessionRequestMapper,
                                         RentalSessionResponseMapper rentalSessionResponseMapper,
                                         ScooterService scooterService,
                                         SeasonTicketService seasonTicketService,
                                         JwtTokenData jwtTokenData, UserService userService) {
        super(jwtTokenData);
        this.rentalSessionService = rentalSessionService;
        this.rentalSessionValidator = rentalSessionValidator;
        this.rentalSessionRequestMapper = rentalSessionRequestMapper;
        this.rentalSessionResponseMapper = rentalSessionResponseMapper;
        this.scooterService = scooterService;
        this.seasonTicketService = seasonTicketService;
        this.promoCodService = promoCodService;
        this.userService = userService;
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


    //ToDo REFACTORING!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    public ResponseEntity<?> createOrUpdate(RentalSessionRequestDTO rentalSessionRequestDTO,
                                            BindingResult bindingResult,
                                            String token)
            throws NotFoundEntityException {


        System.out.println("\n\n\n\n\n\n\n\n");
        System.out.println("rentalSessionRequestDTO = " + rentalSessionRequestDTO);
        System.out.println("\n\n\n\n\n\n\n\n");
        Optional<User> optionalUser = userService.retrieveUserById(rentalSessionRequestDTO.getUserId());
        Optional<Scooter> optionalScooter = scooterService.retrieveScooterBySerialNumber(rentalSessionRequestDTO.getScooterSerialNumber());


        RentalSession rentalSession = rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO,
                optionalUser.get(),
                optionalScooter.get(),
                0,
                null,
                null);



        System.out.println("\n\n\n\n\n\n\n\n");
        System.out.println("rentalSession = " + rentalSession);
        System.out.println("\n\n\n\n\n\n\n\n");

        return ResponseEntity.ok(rentalSessionService.save(rentalSession));
//        if (rentalSessionValidator.isValid(rentalSessionRequestDTO)) {
//            Optional<RentalSession> optionalRentalSession =
//                    rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(rentalSessionRequestDTO.getUserId(),
//                            rentalSessionRequestDTO.getScooterSerialNumber(),
//                            rentalSessionRequestDTO.getBeginDate(),
//                            rentalSessionRequestDTO.getBeginTime());
//            if (optionalRentalSession.isEmpty()) {
//                return save(rentalSessionRequestDTO);
//            } else if (optionalRentalSession.get().getEndDate() != null &&
//                    (isAdmin(token) || isThisUserById(token, rentalSessionRequestDTO.getUserId()))) {
//                return save(rentalSessionRequestDTO);
//            } else {
//                return new ResponseEntity<>("Completed rental session is not available for editing", HttpStatus.FORBIDDEN);
//            }
//        } else {
//            return new ResponseEntity<>("Rental session is not valid", HttpStatus.FORBIDDEN);
//        }
    }

//    private RentalSession toEntity(RentalSessionRequestDTO rentalSessionRequestDTO) throws NotFoundEntityException {
//        com.senla.srs.entity.User user = userService.retrieveUserById(rentalSessionRequestDTO.getUserId())
//                .orElseThrow(() -> new NotFoundEntityException("User"));
//
//        Scooter scooter = scooterService.retrieveScooterBySerialNumber(rentalSessionRequestDTO.getScooterSerialNumber())
//                .orElseThrow(() -> new NotFoundEntityException("Scooter"));
//
//        SeasonTicket seasonTicket = rentalSessionRequestDTO.getSeasonTicketId() != null
//                ? seasonTicketService.retrieveSeasonTicketsById(rentalSessionRequestDTO.getSeasonTicketId())
//                .orElseThrow(() -> new NotFoundEntityException("Season"))
//                : null;
//
//        PromoCod promoCod = rentalSessionRequestDTO.getPromoCodName() != null
//                ? promoCodService.retrievePromoCodByName(rentalSessionRequestDTO.getPromoCodName())
//                .orElseThrow(() -> new NotFoundEntityException("PromoCod"))
//                : null;
//
//        return rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO, user, scooter, seasonTicket, promoCod);
//    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (optionalRentalSession.isPresent()
//                && optionalRentalSession.get().getEndDate() == null
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

//    private ResponseEntity<?> save(RentalSessionRequestDTO rentalSessionRequestDTO) throws NotFoundEntityException {
//        RentalSession rentalSession = toEntity(rentalSessionRequestDTO);
//
//        changeEntityState(rentalSession);
//
//        RentalSession createdRentalSession = rentalSessionService.save(rentalSession);
//
//        return ResponseEntity.ok(rentalSessionResponseMapper.toDto(createdRentalSession));
//    }

    private void changeEntityState(RentalSession rentalSession) {
        Scooter scooter = rentalSession.getScooter();
        SeasonTicket seasonTicket = rentalSession.getSeasonTicket();
        com.senla.srs.entity.User user = rentalSession.getUser();

//        if (rentalSession.getEndDate() == null) {
//            scooter.setStatus(ScooterStatus.USED);
//
//            if (seasonTicket != null) {
//                seasonTicket.setAvailableForUse(false);
//            }
//
//        } else {
//            user.setBalance(user.getBalance() - calculateRate(rentalSession));
//
//            scooter.setStatus(ScooterStatus.AVAILABLE);
//
//            int usageTime = (int) (Duration.between(rentalSession.getBeginDate(), rentalSession.getEndDate()).getSeconds() / 60);
//            scooter.setTimeMillage(scooter.getTimeMillage() + usageTime);
//
//            if (seasonTicket != null && seasonTicket.getRemainingTime() > 0) {
//                seasonTicket.setAvailableForUse(true);
//            }

//        }
    }

    private int calculateRate(RentalSession rentalSession) {
//        LocalDateTime begin = LocalDateTime.of(rentalSession.getBeginDate(), rentalSession.getBeginTime());
//        LocalDateTime end = LocalDateTime.of(rentalSession.getEndDate(), rentalSession.getBeginTime());
//        int usageTime = (int) (Duration.between(begin, end).getSeconds() / 60);
//
//        int pricePerMinute = rentalSession.getScooter().getType().getPricePerMinute();
//
//        int billableTime = calculateBillableTime(rentalSession.getSeasonTicket(), usageTime);
//
//        int priceWithoutPromoCod = billableTime * pricePerMinute;
//
//        return applyPromoCod(priceWithoutPromoCod, rentalSession.getPromoCod(), rentalSession.getUser());
        return 0;
    }

//    private int calculateBillableTime(SeasonTicket seasonTicket, int usageTime) {
//        if (seasonTicket != null) {
//
//            if (seasonTicket.getRemainingTime() >= usageTime) {
//                seasonTicket.setRemainingTime(seasonTicket.getRemainingTime() - usageTime);
//                usageTime = 0;
//            } else {
//                usageTime -= seasonTicket.getRemainingTime();
//                seasonTicket.setRemainingTime(0);
//            }
//
//            if (seasonTicket.getRemainingTime() == 0) {
//                seasonTicket.setAvailableForUse(false);
//            }
//
//        }
//
//        return usageTime;
//    }

//    private int applyPromoCod(int rate, PromoCod promoCod, com.senla.srs.entity.User user) {
//        int discountPercentage = 0;
//
//        if (promoCod != null) {
//
//            if (promoCod.getBonusPoint() > 0) {
//                user.setBalance(user.getBalance() + promoCod.getBonusPoint());
//            }
//
//            if (promoCod.getDiscountPercentage() > 0) {
//                discountPercentage = promoCod.getDiscountPercentage();
//            }
//
//            promoCod.setAvailable(false);
//        }
//
//        return rate * (1 - discountPercentage / 100);
//    }
}
