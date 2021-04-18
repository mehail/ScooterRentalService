package com.senla.srs.controller.v1;

import com.senla.srs.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.mapper.SeasonTicketFullResponseMapper;
import com.senla.srs.mapper.SeasonTicketRequestMapper;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/season_tickets")
public class SeasonTicketController extends AbstractRestController{
    private final SeasonTicketService seasonTicketService;
    private final ScooterTypeService scooterTypeService;
    private final SeasonTicketRequestMapper seasonTicketRequestMapper;
    private final SeasonTicketFullResponseMapper seasonTicketFullResponseMapper;

    private final int duration;
    private static final String NO_SEASON_TICKET_WITH_ID = "A season ticket with this id was not found";

    public SeasonTicketController(UserService userService,
                                  SeasonTicketService seasonTicketService,
                                  ScooterTypeService scooterTypeService,
                                  SeasonTicketRequestMapper seasonTicketRequestMapper,
                                  SeasonTicketFullResponseMapper seasonTicketFullResponseMapper,
                                  @Value("${srs.season.duration:365}") int duration) {
        super(userService);
        this.seasonTicketService = seasonTicketService;
        this.scooterTypeService = scooterTypeService;
        this.seasonTicketRequestMapper = seasonTicketRequestMapper;
        this.seasonTicketFullResponseMapper = seasonTicketFullResponseMapper;
        this.duration = duration;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public List<SeasonTicketFullResponseDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        return isAdmin(userSecurity)
                ? mapListToDtoList(seasonTicketService.retrieveAllSeasonTickets())
                : mapListToDtoList(seasonTicketService.retrieveAllSeasonTicketsByUserId(getAuthUserId(userSecurity)));
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
        Optional<SeasonTicket> optionalSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (isAdmin(userSecurity) || isThisUserSeasonTicket(optionalSeasonTicket, userSecurity)) {

            return optionalSeasonTicket.isPresent()
                    ? ResponseEntity.ok(seasonTicketFullResponseMapper.toDto(optionalSeasonTicket.get()))
                    : new ResponseEntity<>(NO_SEASON_TICKET_WITH_ID, HttpStatus.FORBIDDEN);

        } else {
            return new ResponseEntity<>("Another user's season ticket is requested", HttpStatus.FORBIDDEN);
        }
    }

    private boolean isThisUserSeasonTicket(Optional<SeasonTicket> optionalSeasonTicket,
                                           org.springframework.security.core.userdetails.User userSecurity) {

        return optionalSeasonTicket.isPresent() &&
                isThisUserById(userSecurity, optionalSeasonTicket.get().getUserId());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public ResponseEntity<?> create(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                    @RequestBody SeasonTicketRequestDTO seasonTicketRequestDTO) {
        Optional<User> optionalUser = userService.retrieveUserById(seasonTicketRequestDTO.getUserId());
        Optional<ScooterType> optionalScooterType =
                scooterTypeService.retrieveScooterTypeById(seasonTicketRequestDTO.getScooterTypeId());

        if (isValid(seasonTicketRequestDTO, optionalUser, optionalScooterType)) {

            return isCanSave(userSecurity, seasonTicketRequestDTO)
                    ? save(seasonTicketRequestDTO, optionalUser, optionalScooterType)
                    : new ResponseEntity<>("Modification of the existing season ticket is prohibited", HttpStatus.FORBIDDEN);

        } else {
            return new ResponseEntity<>("Season ticket is not valid", HttpStatus.FORBIDDEN);
        }
    }

    private boolean isValid(SeasonTicketRequestDTO seasonTicketRequestDTO,
                            Optional<User> optionalUser,
                            Optional<ScooterType> optionalScooterType) {

        return optionalUser.isPresent() &&
                optionalUser.get().getBalance() >= seasonTicketRequestDTO.getPrice() &&
                optionalScooterType.isPresent();
    }

    private boolean isCanSave(org.springframework.security.core.userdetails.User userSecurity,
                              SeasonTicketRequestDTO seasonTicketRequestDTO) {
        return getExistOptionalSeasonTicket(seasonTicketRequestDTO).isEmpty() &&
                (isAdmin(userSecurity) || isThisUserById(userSecurity, seasonTicketRequestDTO.getUserId()));
    }

    private ResponseEntity<?> save(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                   Optional<User> optionalUser,
                                   Optional<ScooterType> optionalScooterType) {

        int remainingTime = 0;

        if (optionalScooterType.isPresent()) {
            remainingTime = calculateRemainingTime(seasonTicketRequestDTO, optionalScooterType.get());
        }

        optionalUser.ifPresent(user -> user.setBalance(user.getBalance() - seasonTicketRequestDTO.getPrice()));

        SeasonTicket seasonTicket =
                seasonTicketService.save(seasonTicketRequestMapper.toConsistencySeasonTicket(seasonTicketRequestDTO,
                        remainingTime,
                        duration));

        return ResponseEntity.ok(seasonTicketFullResponseMapper.toDto(seasonTicket));
    }

    private Optional<SeasonTicket> getExistOptionalSeasonTicket(SeasonTicketRequestDTO seasonTicketRequestDTO) {
        return seasonTicketService.retrieveSeasonTicketByUserIdAndScooterTypeIdAndStartDate(
                seasonTicketRequestDTO.getUserId(),
                seasonTicketRequestDTO.getScooterTypeId(),
                seasonTicketRequestDTO.getStartDate()
        );
    }

    private int calculateRemainingTime(SeasonTicketRequestDTO seasonTicketRequestDTO, ScooterType scooterType) {
        int pricePerMinute = scooterType.getPricePerMinute();
        int price = seasonTicketRequestDTO.getPrice();

        return pricePerMinute * price;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('seasonTickets:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<SeasonTicket> optionalSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (optionalSeasonTicket.isPresent()) {
            if (optionalSeasonTicket.get().getAvailableForUse()) {
                try {
                    seasonTicketService.deleteById(id);
                    return new ResponseEntity<>("Season ticket with this id was deleted", HttpStatus.ACCEPTED);
                } catch (EmptyResultDataAccessException e) {
                    log.error(e.getMessage(), NO_SEASON_TICKET_WITH_ID);
                    return new ResponseEntity<>(NO_SEASON_TICKET_WITH_ID, HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Season Ticket isn't available and cannot be deleted", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Season ticket with this id not available for deletion",
                    HttpStatus.FORBIDDEN);
        }
    }
}
