package com.senla.srs.controller.v1;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.mapper.RentalSessionRequestMapper;
import com.senla.srs.mapper.RentalSessionResponseMapper;
import com.senla.srs.model.RentalSession;
import com.senla.srs.model.User;
import com.senla.srs.service.RentalSessionService;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/rental_sessions")
public class RentalSessionController {
    private RentalSessionService rentalSessionService;
    private UserService userService;
    private RentalSessionRequestMapper rentalSessionRequestMapper;
    private RentalSessionResponseMapper rentalSessionResponseMapper;
    private static final String NO_RENTAL_SESSION_WITH_ID = "A rental session with this id was not found";

    @GetMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public List<RentalSessionResponseDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        return userService.isAdmin(userSecurity)
                ? mapListToDtoList(rentalSessionService.retrieveAllRentalSessions())
                : mapListToDtoList(rentalSessionService.retrieveAllRentalSessionsByUserId(userService.getAuthUserId(userSecurity)));
    }

    private Optional<User> getOptionalAuthUser(org.springframework.security.core.userdetails.User userSecurity) {
        return userService.retrieveUserByAuthenticationPrincipal(userSecurity);
    }

    private List<RentalSessionResponseDTO> mapListToDtoList(List<RentalSession> rentalSessions) {
        return rentalSessions.stream()
                .map(seasonTicket -> rentalSessionResponseMapper.toDto(seasonTicket))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> getById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                     @PathVariable Long id) {

        if (userService.isAdmin(userSecurity) || userService.isThisUser(userSecurity, id)) {
            Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

            return optionalRentalSession.isPresent()
                    ? ResponseEntity.ok(rentalSessionResponseMapper.toDto(optionalRentalSession.get()))
                    : new ResponseEntity<>(NO_RENTAL_SESSION_WITH_ID, HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Unauthorized user session requested", HttpStatus.FORBIDDEN);
        }
    }

    private boolean isThisUserRentalSession(RentalSession rentalSession,
                                            org.springframework.security.core.userdetails.User userSecurity) {

        Optional<User> optionalAuthUser = getOptionalAuthUser(userSecurity);

        return optionalAuthUser.isPresent()
                && optionalAuthUser.get().getId().equals(rentalSession.getUser().getId());
    }


















    @PostMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> createOrUpdate(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                            @RequestBody RentalSessionRequestDTO rentalSessionRequestDTO) {

        Optional<RentalSession> optionalExistRentalSession =
                rentalSessionService.retrieveRentalSessionByUserAndScooterAndBegin(rentalSessionRequestMapper
                        .toEntity(rentalSessionRequestDTO));

        if (optionalExistRentalSession.isEmpty()) {
            return create(rentalSessionRequestDTO);
        } else {
            RentalSession existRentalSession = optionalExistRentalSession.get();

            if (existRentalSession.getEnd() != null) {
                return new ResponseEntity<>("Rental session closed and not available for modification", HttpStatus.FORBIDDEN);
            } else if (userService.isAdmin(userSecurity) || isThisUserRentalSession(existRentalSession, userSecurity)) {
                return create(rentalSessionRequestDTO);
            } else {
                return new ResponseEntity<>("Changes to this Rental session are not available", HttpStatus.FORBIDDEN);
            }
        }
    }

    private ResponseEntity<?> create(RentalSessionRequestDTO rentalSessionRequestDTO) {
        if (isValidCreateRentalSession(rentalSessionRequestDTO)) {
            //ToDo Добавить расчет, различное поведение при налиичии даты окончания
            return save(rentalSessionRequestDTO);
        } else {
            return new ResponseEntity<>("Rental session is not valid", HttpStatus.FORBIDDEN);
        }
    }

    private boolean isValidCreateRentalSession(RentalSessionRequestDTO rentalSessionRequestDTO) {
        return rentalSessionRequestDTO.getEnd() != null &&
                isValidSeasonTicket(rentalSessionRequestDTO) &&
                isValidPromoCod(rentalSessionRequestDTO);
    }

    //ToDo Переписать
    private boolean isValidPromoCod(RentalSessionRequestDTO rentalSessionRequestDTO) {
//        PromoCodDTO promoCodDTO = rentalSessionRequestDTO.getPromoCod();
//
//        return promoCodDTO == null || promoCodDTO.getAvailable() &&
//                isValidDate(rentalSessionRequestDTO, promoCodDTO.getStartDate(), promoCodDTO.getExpiredDate());
        return true;
    }

    //ToDo Переписать
    private boolean isValidSeasonTicket(RentalSessionRequestDTO rentalSessionRequestDTO) {
//        UserCompactResponseDTO userCompactResponseDTO = rentalSessionRequestDTO.getUser();
//        SeasonTicketRequestDTO seasonTicketDTO = rentalSessionRequestDTO.getSeasonTicket();
//
//        if (seasonTicketDTO == null) {
//            return true;
//        } else {
//            boolean isThisUser = userCompactResponseDTO.getId().equals(seasonTicketDTO.getUserId());
//            boolean isValidScooterType = rentalSessionRequestDTO.getScooter().getType()
//                    .equals(seasonTicketDTO.getScooterTypeId());
//
//            return isThisUser && isValidScooterType;
//        }
        return true;
    }

    private boolean isValidDate(RentalSessionRequestDTO rentalSessionRequestDTO, LocalDate begin, LocalDate end) {
        return begin.isBefore(rentalSessionRequestDTO.getBegin()) &&
                (end == null || end.isAfter(rentalSessionRequestDTO.getBegin()));
    }


    private ResponseEntity<?> save(RentalSessionRequestDTO rentalSessionRequestDTO) {
        RentalSession rentalSession = rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO);
        rentalSessionService.save(rentalSession);
        try {
            RentalSession createdRentalSession = rentalSessionService.retrieveRentalSessionById(rentalSession.getId()).get();
            return ResponseEntity.ok(rentalSessionResponseMapper.toDto(createdRentalSession));
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
            if (rentalSessionService.retrieveRentalSessionById(id).get().getEnd() != null) {
                rentalSessionService.deleteById(id);
                return new ResponseEntity<>("Rental session with this id was deleted", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Rental session closed and cannot be deleted", HttpStatus.FORBIDDEN);
            }
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), NO_RENTAL_SESSION_WITH_ID);
            return new ResponseEntity<>(NO_RENTAL_SESSION_WITH_ID, HttpStatus.FORBIDDEN);
        }
    }
}
