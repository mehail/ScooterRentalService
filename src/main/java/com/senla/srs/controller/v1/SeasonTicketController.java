package com.senla.srs.controller.v1;

import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketResponseDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Value("${srs.availability}")
    private Integer availabilitySeasonTicket;
    private static final String NO_SEASON_TICKET_WITH_ID = "No season ticket with this ID found";

    @GetMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public List<SeasonTicketResponseDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        if (userService.isAdmin(userSecurity)) {
            return mapListToDtoList(seasonTicketService.retrieveAllSeasonTickets());
        } else {
            Optional<User> optionalAuthThisUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity);

            return optionalAuthThisUser.isPresent()
                    ? mapListToDtoList(seasonTicketService.retrieveAllSeasonTicketsByUserId(optionalAuthThisUser.get().getId()))
                    : new ArrayList<>();
        }
    }

    private List<SeasonTicketResponseDTO> mapListToDtoList(List<SeasonTicket> seasonTickets) {
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
        Optional<User> optionalAuthUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity);

        return optionalAuthUser.isPresent() && seasonTicket.getUserId().equals(optionalAuthUser.get().getId());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public ResponseEntity<?> create(@RequestBody SeasonTicketRequestDTO seasonTicketRequestDTO) {
        if (getExistOptionalSeasonTicket(seasonTicketRequestDTO).isEmpty()) {
            return validateAndSave(seasonTicketRequestDTO);
        } else {
            return new ResponseEntity<>("Modification of the existing season ticket is prohibited", HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<?> validateAndSave(SeasonTicketRequestDTO seasonTicketRequestDTO) {
        Optional<User> optionalUser = userService.retrieveUserById(seasonTicketRequestDTO.getUserId());
        Integer price = calculatePrice(seasonTicketRequestDTO);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("The specified user does not exist", HttpStatus.FORBIDDEN);
        } else if (!isPayable(optionalUser, price)) {
            return new ResponseEntity<>("The user has insufficient balance", HttpStatus.FORBIDDEN);
        } else {
            User user = optionalUser.get();
            user.setBalance(user.getBalance() - price);
        }

        seasonTicketService.save(seasonTicketRequestMapper.toConsistencySeasonTicket(seasonTicketRequestDTO, price,
                availabilitySeasonTicket));

        Optional<SeasonTicket> optionalCreatedSeasonTicket = getExistOptionalSeasonTicket(seasonTicketRequestDTO);

        return optionalCreatedSeasonTicket.isPresent()
                ? ResponseEntity.ok(seasonTicketResponseMapper.toDto(optionalCreatedSeasonTicket.get()))
                : new ResponseEntity<>("The season ticket is not created", HttpStatus.FORBIDDEN);
    }

    private Optional<SeasonTicket> getExistOptionalSeasonTicket(SeasonTicketRequestDTO seasonTicketRequestDTO) {
        return seasonTicketService.retrieveSeasonTicketByUserIdAndScooterTypeAndStartDate(
                seasonTicketRequestDTO.getUserId(),
                scooterTypeRequestMapper.toEntity(seasonTicketRequestDTO.getScooterType()),
                seasonTicketRequestDTO.getStartDate()
        );
    }

    private Integer calculatePrice(SeasonTicketRequestDTO seasonTicketRequestDTO) {
        Integer pricePerMinute = seasonTicketRequestDTO.getScooterType().getPricePerMinute();
        Integer remainingTime = seasonTicketRequestDTO.getRemainingTime();

        return pricePerMinute * remainingTime;
    }

    private boolean isPayable(Optional<User> optionalUser, Integer price) {
        return optionalUser.isPresent() && optionalUser.get().getBalance() >= price;
    }
}
