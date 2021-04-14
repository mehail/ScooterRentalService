package com.senla.srs.controller.v1;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.mapper.RentalSessionRequestMapper;
import com.senla.srs.mapper.RentalSessionResponseMapper;
import com.senla.srs.model.*;
import com.senla.srs.service.RentalSessionService;
import com.senla.srs.service.RentalSessionValidationService;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/rental_sessions")
public class RentalSessionController {
    private final RentalSessionService rentalSessionService;
    private final RentalSessionValidationService rentalSessionValidationService;
    private final ScooterService scooterService;
    private final UserService userService;
    private final RentalSessionRequestMapper rentalSessionRequestMapper;
    private final RentalSessionResponseMapper rentalSessionResponseMapper;

    private static final String NO_RENTAL_SESSION_WITH_ID = "A rental session with this id was not found";

    @GetMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public List<RentalSessionResponseDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        return userService.isAdmin(userSecurity)
                ? mapListToDtoList(rentalSessionService.retrieveAllRentalSessions())
                : mapListToDtoList(rentalSessionService.retrieveAllRentalSessionsByUserId(userService.getAuthUserId(userSecurity)));
    }

    private List<RentalSessionResponseDTO> mapListToDtoList(List<RentalSession> rentalSessions) {
        return rentalSessions.stream()
                .map(rentalSessionResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> getById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                     @PathVariable Long id) {

        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (userService.isAdmin(userSecurity) || isThisUserRentalSession(optionalRentalSession, userSecurity)) {

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
                userService.isThisUser(userSecurity, optionalRentalSession.get().getUser().getId());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> createOrUpdate(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                            @RequestBody RentalSessionRequestDTO rentalSessionRequestDTO) {
        if (rentalSessionValidationService.isValid(rentalSessionRequestDTO)) {
            Optional<RentalSession> optionalRentalSession =
                    rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(rentalSessionRequestDTO.getUserId(),
                            rentalSessionRequestDTO.getScooterSerialNumber(),
                            rentalSessionRequestDTO.getBegin());
            if (optionalRentalSession.isEmpty()) {
                return create(rentalSessionRequestDTO);
            } else if (optionalRentalSession.get().getEnd() != null &&
                    (userService.isAdmin(userSecurity) ||
                            userService.isThisUser(userSecurity, rentalSessionRequestDTO.getUserId()))) {
                return create(rentalSessionRequestDTO);
            } else {
                return new ResponseEntity<>("Completed rental session is not available for editing", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Rental session is not valid", HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<?> create(RentalSessionRequestDTO rentalSessionRequestDTO) {
        RentalSession rentalSession = rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO);
        changeEntityState(rentalSession);
        rentalSessionService.save(rentalSession);

        Optional<RentalSession> optionalRentalSession =
                rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(rentalSessionRequestDTO.getUserId(),
                        rentalSessionRequestDTO.getScooterSerialNumber(),
                        rentalSessionRequestDTO.getBegin());

        return optionalRentalSession.isPresent()
                ? ResponseEntity.ok(rentalSessionResponseMapper.toDto(optionalRentalSession.get()))
                : new ResponseEntity<>("The rental session is not created", HttpStatus.FORBIDDEN);
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
            rentalSession.getScooter().setStatus(ScooterStatus.AVAILABLE);

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
                rentalSessionService.deleteById(id);
                return new ResponseEntity<>("Rental session with this id was deleted", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Rental session closed and cannot be deleted", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(NO_RENTAL_SESSION_WITH_ID, HttpStatus.FORBIDDEN);
        }
    }
}
