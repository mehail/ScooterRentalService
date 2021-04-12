package com.senla.srs.controller.v1;

import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.mapper.RentalSessionRequestMapper;
import com.senla.srs.mapper.RentalSessionResponseMapper;
import com.senla.srs.model.RentalSession;
import com.senla.srs.service.RentalSessionService;
import com.senla.srs.service.ScooterService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/rental_sessions")
public class RentalSessionController {
    private RentalSessionService rentalSessionService;
    private ScooterService scooterService;
    private UserService userService;
    private RentalSessionRequestMapper rentalSessionRequestMapper;
    private RentalSessionResponseMapper rentalSessionResponseMapper;
    private static final String NO_RENTAL_SESSION_WITH_ID = "A rental session with this id was not found";

    @GetMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public List<RentalSessionResponseDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        testing();

        return userService.isAdmin(userSecurity)
                ? mapListToDtoList(rentalSessionService.retrieveAllRentalSessions())
                : mapListToDtoList(rentalSessionService.retrieveAllRentalSessionsByUserId(userService.getAuthUserId(userSecurity)));
    }

    private void testing() {
        Optional<RentalSession> rentalSession = rentalSessionService.retrieveRentalSessionById(1L);
        RentalSession rentalSession1 = rentalSession.get();

        soutt("1 "+rentalSession1.getBegin().toString() + " " + rentalSession1.getBegin().getClass());
//ToDo ошибка в парсинге LocalDate!!!


//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
//        LocalDateTime localDate = LocalDateTime.parse("2021-04-01 12:10:00",formatter);
//        soutt("2 " + localDate.toString() + " " + localDate.getClass());
//
//        soutt("1"+rentalSession1.toString());
//        soutt("2" + rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(rentalSession1.getUser().getId(),
//                rentalSession1.getScooter().getSerialNumber(), rentalSession1.getBegin()));
    }

    private void soutt(String s){
        System.out.println("\n\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n\n"+s+"\n\n\n");
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

//    @PostMapping
//    @PreAuthorize("hasAuthority('rentalSessions:read')")
//    public ResponseEntity<?> createOrUpdate(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
//                                            @RequestBody RentalSessionRequestDTO rentalSessionRequestDTO) {
//
//        if (userService.isAdmin(userSecurity) ||
//                userService.isThisUser(userSecurity, rentalSessionRequestDTO.getUserId())) {
//
//            Optional<User> optionalUser = userService.retrieveUserById(rentalSessionRequestDTO.getUserId());
//            if (optionalUser.isEmpty()) {
//                return new ResponseEntity<>("User is not correct", HttpStatus.FORBIDDEN);
//            }
//
//            Optional<Scooter> optionalScooter =
//                    scooterService.retrieveScooterBySerialNumber(rentalSessionRequestDTO.getScooterSerialNumber());
//            if (optionalScooter.isEmpty()) {
//                new ResponseEntity<>("Scooter is not correct", HttpStatus.FORBIDDEN);
//            }
//
//            Optional<RentalSession> optionalRentalSession =
//                    rentalSessionService.retrieveRentalSessionByUserAndScooterAndBegin(optionalUser.get(),
//                            optionalScooter.get(), rentalSessionRequestDTO.getBegin());
//
//            if (optionalRentalSession.isPresent()) {
//                RentalSession existRentalSession = optionalRentalSession.get();
//
//                if (existRentalSession.getEnd() != null) {
//                    return update(rentalSessionRequestDTO, existRentalSession);
//                } else {
//                    return new ResponseEntity<>("Changing a closed session is impossible",
//                            HttpStatus.FORBIDDEN);
//                }
//
//            } else if (isValidCreateRentalSession(rentalSessionRequestDTO)){
//                return create(rentalSessionRequestDTO);
//            } else {
//                return new ResponseEntity<>("The rental session is not correct",
//                        HttpStatus.FORBIDDEN);
//            }
//
//        } else {
//            return new ResponseEntity<>("To make changes, re-log in as an administrator or session owner",
//                    HttpStatus.FORBIDDEN);
//        }
//    }
//
//    private ResponseEntity<?> create(RentalSessionRequestDTO rentalSessionRequestDTO) {
//        rentalSessionService.save(rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO));
//        Optional<RentalSession> optionalRentalSession =
//    }
//
//    private ResponseEntity<?> update(RentalSessionRequestDTO rentalSessionRequestDTO, RentalSession rentalSession) {
//        rentalSession.setEnd(rentalSessionRequestDTO.getEnd());
//        int rate = calculate(rentalSession);
//        rentalSession.setRate(rate);
//        updateEntitiesToClosedRentalSession(rentalSession, rate);
//        return save(rentalSession);
//    }
//
//    private ResponseEntity<?> save(RentalSession rentalSession) {
//        rentalSessionService.save(rentalSession);
//        Optional<RentalSession> optionalRentalSession =
//                rentalSessionService.retrieveRentalSessionById(rentalSession.getId());
//
//        return optionalRentalSession.isPresent()
//                ? ResponseEntity.ok(rentalSessionResponseMapper.toDto(optionalRentalSession.get()))
//                : new ResponseEntity<>("The rental session is not created/updated", HttpStatus.FORBIDDEN);
//    }






//
//    @PostMapping
//    @PreAuthorize("hasAuthority('rentalSessions:read')")
//    public ResponseEntity<?> createOrUpdate(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
//                                            @RequestBody RentalSessionRequestDTO rentalSessionRequestDTO) {
//
//        Optional<RentalSession> optionalExistRentalSession =
//                rentalSessionService.retrieveRentalSessionByUserAndScooterAndBegin(, rentalSessionRequestMapper
//                        .toEntity(rentalSessionRequestDTO), , );
//
//        if (optionalExistRentalSession.isEmpty()) {
//            return create(rentalSessionRequestDTO);
//        } else {
//            RentalSession existRentalSession = optionalExistRentalSession.get();
//
//            if (existRentalSession.getEnd() != null) {
//                return new ResponseEntity<>("Rental session closed and not available for modification", HttpStatus.FORBIDDEN);
//            } else if (userService.isAdmin(userSecurity) || isThisUserRentalSession(existRentalSession, userSecurity)) {
//                return create(rentalSessionRequestDTO);
//            } else {
//                return new ResponseEntity<>("Changes to this Rental session are not available", HttpStatus.FORBIDDEN);
//            }
//        }
//    }
//
//    private ResponseEntity<?> create(RentalSessionRequestDTO rentalSessionRequestDTO) {
//        if (isValidCreateRentalSession(rentalSessionRequestDTO)) {
//            //ToDo Добавить расчет, различное поведение при налиичии даты окончания
//            return save(rentalSessionRequestDTO);
//        } else {
//            return new ResponseEntity<>("Rental session is not valid", HttpStatus.FORBIDDEN);
//        }
//    }
//
//    private boolean isValidCreateRentalSession(RentalSessionRequestDTO rentalSessionRequestDTO) {
//        return rentalSessionRequestDTO.getEnd() != null &&
//                isValidSeasonTicket(rentalSessionRequestDTO) &&
//                isValidPromoCod(rentalSessionRequestDTO);
//    }
//
//    //ToDo Переписать
//    private boolean isValidPromoCod(RentalSessionRequestDTO rentalSessionRequestDTO) {
////        PromoCodDTO promoCodDTO = rentalSessionRequestDTO.getPromoCod();
////
////        return promoCodDTO == null || promoCodDTO.getAvailable() &&
////                isValidDate(rentalSessionRequestDTO, promoCodDTO.getStartDate(), promoCodDTO.getExpiredDate());
//        return true;
//    }
//
//    //ToDo Переписать
//    private boolean isValidSeasonTicket(RentalSessionRequestDTO rentalSessionRequestDTO) {
////        UserCompactResponseDTO userCompactResponseDTO = rentalSessionRequestDTO.getUser();
////        SeasonTicketRequestDTO seasonTicketDTO = rentalSessionRequestDTO.getSeasonTicket();
////
////        if (seasonTicketDTO == null) {
////            return true;
////        } else {
////            boolean isThisUser = userCompactResponseDTO.getId().equals(seasonTicketDTO.getUserId());
////            boolean isValidScooterType = rentalSessionRequestDTO.getScooter().getType()
////                    .equals(seasonTicketDTO.getScooterTypeId());
////
////            return isThisUser && isValidScooterType;
////        }
//        return true;
//    }
//
//    private boolean isValidDate(RentalSessionRequestDTO rentalSessionRequestDTO, LocalDate begin, LocalDate end) {
//        return begin.isBefore(rentalSessionRequestDTO.getBegin()) &&
//                (end == null || end.isAfter(rentalSessionRequestDTO.getBegin()));
//    }
//
//
//    private ResponseEntity<?> save(RentalSessionRequestDTO rentalSessionRequestDTO) {
//        RentalSession rentalSession = rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO);
//        rentalSessionService.save(rentalSession);
//        try {
//            RentalSession createdRentalSession = rentalSessionService.retrieveRentalSessionById(rentalSession.getId()).get();
//            return ResponseEntity.ok(rentalSessionResponseMapper.toDto(createdRentalSession));
//        } catch (NoSuchElementException e) {
//            String errorMessage = "The rental session is not created";
//            log.error(e.getMessage(), errorMessage);
//            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
//        }
//    }


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
