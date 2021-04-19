package com.senla.srs.controller.v1;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.mapper.RentalSessionRequestMapper;
import com.senla.srs.mapper.RentalSessionResponseMapper;
import com.senla.srs.model.*;
import com.senla.srs.service.RentalSessionService;
import com.senla.srs.service.RentalSessionValidator;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/rental_sessions")
public class RentalSessionController extends AbstractRestController{
    private final RentalSessionService rentalSessionService;
    private final RentalSessionValidator rentalSessionValidator;
    private final RentalSessionRequestMapper rentalSessionRequestMapper;
    private final RentalSessionResponseMapper rentalSessionResponseMapper;
    private final ScooterService scooterService;

    private static final String NO_RENTAL_SESSION_WITH_ID = "A rental session with this id was not found";

    public RentalSessionController(UserService userService,
                                   RentalSessionService rentalSessionService,
                                   RentalSessionValidator rentalSessionValidator,
                                   RentalSessionRequestMapper rentalSessionRequestMapper,
                                   RentalSessionResponseMapper rentalSessionResponseMapper, ScooterService scooterService) {
        super(userService);
        this.rentalSessionService = rentalSessionService;
        this.rentalSessionValidator = rentalSessionValidator;
        this.rentalSessionRequestMapper = rentalSessionRequestMapper;
        this.rentalSessionResponseMapper = rentalSessionResponseMapper;
        this.scooterService = scooterService;
    }

    @GetMapping("/test/")
    public ResponseEntity<?> test() {
        Optional<User> user = userService.retrieveUserById(1L);
        Optional<Scooter> scooter = scooterService.retrieveScooterBySerialNumber("0001X");
        LocalDateTime begin = LocalDateTime.of(2020,1,1,10,0,0);
        LocalDateTime end = LocalDateTime.of(2020,1,1,10,1,0);

        RentalSession rentalSession = new RentalSession(
                null,
                user.get(),
                scooter.get(),
                10,
                begin,
                end,
                null, null
        );

        System.out.println("\n\n\n\n\n rentalSession = " + rentalSession + "\n\n\n\n\n");

        RentalSession created = rentalSessionService.save(rentalSession);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public List<RentalSessionResponseDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        return isAdmin(userSecurity)
                ? rentalSessionResponseMapper.mapListToDtoList(rentalSessionService.retrieveAllRentalSessions())
                : rentalSessionResponseMapper.mapListToDtoList(
                        rentalSessionService.retrieveAllRentalSessionsByUserId(getAuthUserId(userSecurity)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> getById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                     @PathVariable Long id) {

        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (isAdmin(userSecurity) || isThisUserRentalSession(optionalRentalSession, userSecurity)) {

            return optionalRentalSession.isPresent()
                    ? ResponseEntity.ok(rentalSessionResponseMapper.toDto(optionalRentalSession.get()))
                    : new ResponseEntity<>(NO_RENTAL_SESSION_WITH_ID, HttpStatus.FORBIDDEN);

        } else {
            return new ResponseEntity<>("Unauthorized user session requested", HttpStatus.FORBIDDEN);
        }
    }

    private boolean isThisUserRentalSession(Optional<RentalSession> optionalRentalSession,
                                            org.springframework.security.core.userdetails.User userSecurity) {

        return optionalRentalSession.isPresent() &&
                isThisUserById(userSecurity, optionalRentalSession.get().getUser().getId());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> createOrUpdate(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                            @RequestBody RentalSessionRequestDTO rentalSessionRequestDTO) {
        if (rentalSessionValidator.isValid(rentalSessionRequestDTO)) {
            Optional<RentalSession> optionalRentalSession =
                    rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(rentalSessionRequestDTO.getUserId(),
                            rentalSessionRequestDTO.getScooterSerialNumber(),
                            rentalSessionRequestDTO.getBegin());
            if (optionalRentalSession.isEmpty()) {
                return save(rentalSessionRequestDTO);
            } else if (optionalRentalSession.get().getEnd() != null &&
                    (isAdmin(userSecurity) || isThisUserById(userSecurity, rentalSessionRequestDTO.getUserId()))) {
                return save(rentalSessionRequestDTO);
            } else {
                return new ResponseEntity<>("Completed rental session is not available for editing", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Rental session is not valid", HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<?> save(RentalSessionRequestDTO rentalSessionRequestDTO) {
        RentalSession rentalSession = rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO);

        changeEntityState(rentalSession);

        RentalSession createdRentalSession = rentalSessionService.save(rentalSession);

        return ResponseEntity.ok(rentalSessionResponseMapper.toDto(createdRentalSession));
    }

    private void changeEntityState(RentalSession rentalSession) {
        Scooter scooter = rentalSession.getScooter();
        SeasonTicket seasonTicket = rentalSession.getSeasonTicket();
        User user = rentalSession.getUser();

        if (rentalSession.getEnd() == null) {
            scooter.setStatus(ScooterStatus.USED);

            if (seasonTicket != null) {
                seasonTicket.setAvailableForUse(false);
            }

        } else {
            user.setBalance(user.getBalance() - calculateRate(rentalSession));

            scooter.setStatus(ScooterStatus.AVAILABLE);

            int usageTime = (int) (Duration.between(rentalSession.getBegin(), rentalSession.getEnd()).getSeconds() / 60);
            scooter.setTimeMillage(scooter.getTimeMillage() + usageTime);

            if (seasonTicket != null && seasonTicket.getRemainingTime() > 0) {
                seasonTicket.setAvailableForUse(true);
            }

        }
    }

    private int calculateRate(RentalSession rentalSession) {
        LocalDateTime begin = rentalSession.getBegin();
        LocalDateTime end = rentalSession.getEnd();
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

        return rate * (1 - discountPercentage / 100);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (optionalRentalSession.isPresent()) {
            if (optionalRentalSession.get().getEnd() == null) {
                try {
                    rentalSessionService.deleteById(id);
                    return new ResponseEntity<>("Rental session with this id was deleted", HttpStatus.ACCEPTED);
                } catch (EmptyResultDataAccessException e) {
                    log.error(e.getMessage(), NO_RENTAL_SESSION_WITH_ID);
                    return new ResponseEntity<>(NO_RENTAL_SESSION_WITH_ID, HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Rental session closed and cannot be deleted", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Rental session with this id not available for deletion",
                    HttpStatus.FORBIDDEN);
        }
    }
}
