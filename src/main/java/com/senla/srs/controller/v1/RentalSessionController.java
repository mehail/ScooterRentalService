package com.senla.srs.controller.v1;

import com.senla.srs.dto.RentalSessionDTO;
import com.senla.srs.mapper.RentalSessionMapper;
import com.senla.srs.model.RentalSession;
import com.senla.srs.model.User;
import com.senla.srs.service.RentalSessionService;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/rental_sessions")
public class RentalSessionController {
    private RentalSessionService rentalSessionService;
    private UserService userService;
    private RentalSessionMapper rentalSessionMapper;
    private static final String NO_RENTAL_SESSION_WITH_ID = "No rental session with this serial number found";

    @GetMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public List<RentalSessionDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        if (userService.isAdmin(userSecurity)) {
            return rentalSessionService.retrieveAllRentalSessions().stream()
                    .map(rentalSession -> rentalSessionMapper.toDto(rentalSession))
                    .collect(Collectors.toList());
        } else {
            try {
                User authUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity).get();
                return rentalSessionService.retrieveAllRentalSessionsByUserId(authUser.getId()).stream()
                        .map(rentalSession -> rentalSessionMapper.toDto(rentalSession))
                        .collect(Collectors.toList());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage(), NO_RENTAL_SESSION_WITH_ID);
                return new ArrayList<>();
            }
        }

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> getById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                     @PathVariable Long id) {

        try {
            RentalSession rentalSession = rentalSessionService.retrieveRentalSessionById(id).get();

            if (userService.isAdmin(userSecurity) || isThisUserRentalSession(rentalSession, userSecurity)) {
                return getById(id);
            } else {
                return new ResponseEntity<>("Unauthorized user session requested", HttpStatus.FORBIDDEN);
            }
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), NO_RENTAL_SESSION_WITH_ID);
            return new ResponseEntity<>(NO_RENTAL_SESSION_WITH_ID, HttpStatus.FORBIDDEN);
        }
    }

    private boolean isThisUserRentalSession(RentalSession rentalSession,
                                            org.springframework.security.core.userdetails.User userSecurity) {

        User authUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity).get();
        User sessionUser = rentalSession.getUser();

        return authUser.getId().equals(sessionUser.getId());
    }

    private ResponseEntity<?> getById(Long id) {
        try {
            RentalSession rentalSession = rentalSessionService.retrieveRentalSessionById(id).get();
            return ResponseEntity.ok(rentalSessionMapper.toDto(rentalSession));
        } catch (NoSuchElementException e) {
            String errorMessage = NO_RENTAL_SESSION_WITH_ID;
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> createOrUpdate(@RequestBody RentalSessionDTO RentalSessionDTO) {
        //ToDo Plug!!! Add access rights !!!
        RentalSession rentalSession = rentalSessionMapper.toEntity(RentalSessionDTO);
        rentalSessionService.save(rentalSession);
        try {
            RentalSession createdRentalSession = rentalSessionService.retrieveRentalSessionById(rentalSession.getId()).get();
            return ResponseEntity.ok(rentalSessionMapper.toDto(createdRentalSession));
        } catch (NoSuchElementException e) {
            String errorMessage = "The rental session is not created";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            rentalSessionService.deleteById(id);
            return new ResponseEntity<>("Rental session with this id was deleted", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            String errorMessage = "A rental session with this id was not detected";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }
}
