package com.senla.srs.controller.v1;

import com.senla.srs.dto.season.ticket.SeasonTicketRequestDTO;
import com.senla.srs.mapper.ScooterTypeRequestMapper;
import com.senla.srs.mapper.SeasonTicketRequestMapper;
import com.senla.srs.mapper.SeasonTicketResponseMapper;
import com.senla.srs.model.SeasonTicket;
import com.senla.srs.model.User;
import com.senla.srs.service.SeasonTicketService;
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
import java.util.Optional;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/season_tickets")
public class SeasonTicketController {
    private SeasonTicketService seasonTicketService;
    private UserService userService;
    private SeasonTicketRequestMapper seasonTicketRequestMapper;
    private SeasonTicketResponseMapper seasonTicketResponseMapper;
    private ScooterTypeRequestMapper scooterTypeRequestMapper;
    private static final String NO_SEASON_TICKET_WITH_ID = "No season ticket with this ID found";

    @GetMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public List<SeasonTicketRequestDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        if (userService.isAdmin(userSecurity)) {
            return seasonTicketResponseMapper.listToDto(seasonTicketService.retrieveAllSeasonTickets());
        } else {
            try {
                User authUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity).get();
                return seasonTicketResponseMapper.listToDto(seasonTicketService.retrieveAllSeasonTicketsByUserId(authUser.getId()));
            } catch (NoSuchElementException e) {
                log.error(e.getMessage(), NO_SEASON_TICKET_WITH_ID);
                return new ArrayList<>();
            }
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public ResponseEntity<?> getById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                     @PathVariable Long id) {
        Optional<SeasonTicket> optionalExistSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (optionalExistSeasonTicket.isEmpty()) {
            return new ResponseEntity<>(NO_SEASON_TICKET_WITH_ID, HttpStatus.FORBIDDEN);
        } else if (userService.isAdmin(userSecurity) || isThisUser(userSecurity, optionalExistSeasonTicket.get())) {
            return ResponseEntity.ok(optionalExistSeasonTicket.get());
        } else {
            return new ResponseEntity<>("Another user's season ticket is requested", HttpStatus.FORBIDDEN);
        }
    }

    private boolean isThisUser(org.springframework.security.core.userdetails.User userSecurity, SeasonTicket seasonTicket) {
        User authUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity).get();
        return seasonTicket.getUserId().equals(authUser.getId());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public ResponseEntity<?> createOrUpdate(@RequestBody SeasonTicketRequestDTO seasonTicketRequestDTO) {
        Optional<SeasonTicket> optionalSeasonTicket =
                seasonTicketService.retrieveSeasonTicketByUserIdAndScooterTypeAndStartDate(
                        seasonTicketRequestDTO.getUserId(),
                        scooterTypeRequestMapper.toEntity(seasonTicketRequestDTO.getScooterType()),
                        seasonTicketRequestDTO.getStartDate());

        if (optionalSeasonTicket.isEmpty()) {
            SeasonTicket seasonTicket = seasonTicketRequestMapper.toEntity(seasonTicketRequestDTO);
            seasonTicketService.save(seasonTicket);

            try {
                SeasonTicket createdSeasonTicket =
                        seasonTicketService.retrieveSeasonTicketByUserIdAndScooterTypeAndStartDate(
                                seasonTicketRequestDTO.getUserId(),
                                scooterTypeRequestMapper.toEntity(seasonTicketRequestDTO.getScooterType()),
                                seasonTicketRequestDTO.getStartDate()).get();

                return ResponseEntity.ok(seasonTicketResponseMapper.toDto(createdSeasonTicket));
            } catch (NoSuchElementException e) {
                String errorMessage = "The season ticket is not created";
                log.error(e.getMessage(), errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>("Modification of the existing Season Ticket is prohibited", HttpStatus.FORBIDDEN);
        }
    }
}
