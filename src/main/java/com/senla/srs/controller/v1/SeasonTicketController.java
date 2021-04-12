package com.senla.srs.controller.v1;

import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.mapper.SeasonTicketRequestMapper;
import com.senla.srs.mapper.SeasonTicketFullResponseMapper;
import com.senla.srs.model.ScooterType;
import com.senla.srs.model.SeasonTicket;
import com.senla.srs.model.User;
import com.senla.srs.service.ScooterTypeService;
import com.senla.srs.service.SeasonTicketService;
import com.senla.srs.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
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
@RestController
@RequestMapping("/api/v1/season_tickets")
public class SeasonTicketController {
    private final SeasonTicketService seasonTicketService;
    private final ScooterTypeService scooterTypeService;
    private final UserService userService;
    private final SeasonTicketRequestMapper seasonTicketRequestMapper;
    private final SeasonTicketFullResponseMapper seasonTicketFullResponseMapper;

    private int duration;
    private static final String NO_SEASON_TICKET_WITH_ID = "A season ticket with this id was not found";

    public SeasonTicketController(SeasonTicketService seasonTicketService,
                                  ScooterTypeService scooterTypeService,
                                  UserService userService,
                                  SeasonTicketRequestMapper seasonTicketRequestMapper,
                                  SeasonTicketFullResponseMapper seasonTicketFullResponseMapper,
                                  @Value("${srs.season.duration:365}") Integer duration) {
        this.seasonTicketService = seasonTicketService;
        this.scooterTypeService = scooterTypeService;
        this.userService = userService;
        this.seasonTicketRequestMapper = seasonTicketRequestMapper;
        this.seasonTicketFullResponseMapper = seasonTicketFullResponseMapper;
        this.duration = duration;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public List<SeasonTicketFullResponseDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        if (userService.isAdmin(userSecurity)) {
            return mapListToDtoList(seasonTicketService.retrieveAllSeasonTickets());
        } else {
            Optional<User> optionalAuthThisUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity);

            return optionalAuthThisUser.isPresent()
                    ? mapListToDtoList(seasonTicketService.retrieveAllSeasonTicketsByUserId(optionalAuthThisUser.get().getId()))
                    : new ArrayList<>();
        }
    }

    private List<SeasonTicketFullResponseDTO> mapListToDtoList(List<SeasonTicket> seasonTickets) {
        return seasonTickets.stream()
                .map(seasonTicketFullResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public ResponseEntity<?> getById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                     @PathVariable Long id) {
        Optional<SeasonTicket> optionalExistSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (optionalExistSeasonTicket.isEmpty()) {
            return new ResponseEntity<>(NO_SEASON_TICKET_WITH_ID, HttpStatus.FORBIDDEN);
        } else if (userService.isAdmin(userSecurity) ||
                userService.isThisUser(userSecurity, optionalExistSeasonTicket.get().getUserId())) {
            return ResponseEntity.ok(seasonTicketFullResponseMapper.toDto(optionalExistSeasonTicket.get()));
        } else {
            return new ResponseEntity<>("Another user's season ticket is requested", HttpStatus.FORBIDDEN);
        }
    }

    //ToDo Расписать права доступа!!!!!!!!!!!!
    @PostMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public ResponseEntity<?> create(@RequestBody SeasonTicketRequestDTO seasonTicketRequestDTO) {
        if (getExistOptionalSeasonTicket(seasonTicketRequestDTO).isEmpty()) {
            return save(seasonTicketRequestDTO);
        } else {
            return new ResponseEntity<>("Modification of the existing season ticket is prohibited", HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<?> save(SeasonTicketRequestDTO seasonTicketRequestDTO) {
        Optional<User> optionalUser = userService.retrieveUserById(seasonTicketRequestDTO.getUserId());

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("The user incorrect", HttpStatus.FORBIDDEN);
        }

        Optional<ScooterType> optionalScooterType =
                scooterTypeService.retrieveScooterTypeById(seasonTicketRequestDTO.getScooterTypeId());

        if (optionalScooterType.isEmpty()) {
            return new ResponseEntity<>("The scooter type incorrect", HttpStatus.FORBIDDEN);
        }

        Integer price = calculatePrice(seasonTicketRequestDTO, optionalScooterType.get());

        if (!isPayable(optionalUser, price)) {
            return new ResponseEntity<>("The user has insufficient balance", HttpStatus.FORBIDDEN);
        } else {
            User user = optionalUser.get();
            user.setBalance(user.getBalance() - price);
        }

        seasonTicketService.save(seasonTicketRequestMapper.toConsistencySeasonTicket(seasonTicketRequestDTO, price,
                duration));

        Optional<SeasonTicket> optionalCreatedSeasonTicket = getExistOptionalSeasonTicket(seasonTicketRequestDTO);

        return optionalCreatedSeasonTicket.isPresent()
                ? ResponseEntity.ok(seasonTicketFullResponseMapper.toDto(optionalCreatedSeasonTicket.get()))
                : new ResponseEntity<>("The season ticket is not created", HttpStatus.FORBIDDEN);
    }

    private Optional<SeasonTicket> getExistOptionalSeasonTicket(SeasonTicketRequestDTO seasonTicketRequestDTO) {
        return seasonTicketService.retrieveSeasonTicketByUserIdAndScooterTypeIdAndStartDate(
                seasonTicketRequestDTO.getUserId(),
                seasonTicketRequestDTO.getScooterTypeId(),
                seasonTicketRequestDTO.getStartDate()
        );
    }

    private int calculatePrice(SeasonTicketRequestDTO seasonTicketRequestDTO, ScooterType scooterType) {
        int pricePerMinute = scooterType.getPricePerMinute();
        int remainingTime = seasonTicketRequestDTO.getRemainingTime();

        return pricePerMinute * remainingTime;
    }

    private boolean isPayable(Optional<User> optionalUser, Integer price) {
        return optionalUser.isPresent() && optionalUser.get().getBalance() >= price;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('seasonTickets:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Optional<SeasonTicket> optionalSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (optionalSeasonTicket.isPresent() && optionalSeasonTicket.get().getAvailableForUse()) {
            try {
                seasonTicketService.deleteById(id);
                return new ResponseEntity<>("Season ticket with this id was deleted", HttpStatus.ACCEPTED);
            } catch (EmptyResultDataAccessException e) {
                log.error(e.getMessage(), NO_SEASON_TICKET_WITH_ID);
                return new ResponseEntity<>(NO_SEASON_TICKET_WITH_ID, HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Season ticket with this id not available for deletion",
                    HttpStatus.FORBIDDEN);
        }
    }
}
