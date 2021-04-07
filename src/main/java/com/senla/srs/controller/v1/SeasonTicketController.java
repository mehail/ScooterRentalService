package com.senla.srs.controller.v1;

import com.senla.srs.dto.SeasonTicketRequestDTO;
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
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/season_tickets")
public class SeasonTicketController {
    private SeasonTicketService seasonTicketService;
    private SeasonTicketRequestMapper seasonTicketRequestMapper;
    private SeasonTicketResponseMapper seasonTicketResponseMapper;
    private UserService userService;
    private static final String NO_SEASON_TICKET_WITH_ID = "No season ticket with this ID found";

    @GetMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public List<SeasonTicketRequestDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        if (userService.isAdmin(userSecurity)) {
            return convertCollectionSeasonTicketsToResponseDTO(seasonTicketService.retrieveAllSeasonTickets());
        } else {
            try {
                User authUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity).get();
                return convertCollectionSeasonTicketsToResponseDTO(seasonTicketService.retrieveAllSeasonTicketsByUserId(authUser.getId()));
            } catch (NoSuchElementException e) {
                log.error(e.getMessage(), NO_SEASON_TICKET_WITH_ID);
                return new ArrayList<>();
            }
        }
    }

    //ToDo Output to mapper
    private List<SeasonTicketRequestDTO> convertCollectionSeasonTicketsToResponseDTO(List<SeasonTicket> seasonTickets) {
        return seasonTickets.stream()
                .map(seasonTicket -> seasonTicketResponseMapper.toDto(seasonTicket))
                .collect(Collectors.toList());
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
    @PreAuthorize("hasAuthority('seasonTickets:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody SeasonTicketRequestDTO seasonTicketRequestDTO) {
        if (isValidDate(seasonTicketRequestDTO)) {
            return create(seasonTicketRequestDTO);
        } else {
            return new ResponseEntity<>("The start and end dates of the promo code are not correct",
                    HttpStatus.FORBIDDEN);
        }
    }

    //ToDo Rewrite, remove the stub
    private ResponseEntity<?> create(SeasonTicketRequestDTO seasonTicketRequestDTO) {
        return null;
    }


    private boolean isValidDate(SeasonTicketRequestDTO seasonTicketRequestDTO) {
        boolean isCorrectPrice = seasonTicketRequestDTO.getPrice() == seasonTicketRequestDTO.getRemainingTime() *
                        seasonTicketRequestDTO.getScooterType().getPricePerMinute();
        boolean isCorrectDate = seasonTicketRequestDTO.getExpiredDate() == null ||
                seasonTicketRequestDTO.getStartDate().isBefore(seasonTicketRequestDTO.getExpiredDate());

        return isCorrectDate && isCorrectPrice;
    }

}
