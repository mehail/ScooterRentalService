package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.seasonticket.SeasonTicketDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.SeasonTicketFullResponseMapper;
import com.senla.srs.mapper.SeasonTicketRequestMapper;
import com.senla.srs.model.ScooterType;
import com.senla.srs.model.SeasonTicket;
import com.senla.srs.service.ScooterTypeService;
import com.senla.srs.service.SeasonTicketService;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Slf4j
@Controller
public class SeasonTicketControllerFacade extends AbstractFacade implements
        EntityControllerFacade<SeasonTicketDTO, SeasonTicketRequestDTO, SeasonTicketFullResponseDTO, Long> {

    private static final String NO_SEASON_TICKET_WITH_ID = "A season ticket with this id was not found";
    private static final String FORBIDDEN_FOR_DELETE = "Season ticket with this id not available for deletion";
    private final SeasonTicketService seasonTicketService;
    private final ScooterTypeService scooterTypeService;
    private final SeasonTicketRequestMapper seasonTicketRequestMapper;
    private final SeasonTicketFullResponseMapper seasonTicketFullResponseMapper;
    private final int duration;


    public SeasonTicketControllerFacade(UserService userService,
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

    @Override
    public Page<SeasonTicketFullResponseDTO> getAll(Integer page, Integer size, String sort, User userSecurity) {
        return isAdmin(userSecurity)
                ? seasonTicketFullResponseMapper.mapPageToDtoPage(seasonTicketService.retrieveAllSeasonTickets(page, size, sort))

                : seasonTicketFullResponseMapper.mapPageToDtoPage(
                seasonTicketService.retrieveAllSeasonTicketsByUserId(getAuthUserId(userSecurity), page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, User userSecurity) throws NotFoundEntityException {
        Optional<SeasonTicket> optionalSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (isAdmin(userSecurity) || isThisUserSeasonTicket(optionalSeasonTicket, userSecurity)) {
            return new ResponseEntity<>(optionalSeasonTicket
                    .map(seasonTicketFullResponseMapper::toDto)
                    .orElseThrow(() -> new NotFoundEntityException("Season ticket")), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Another user's season ticket is requested", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<?> createOrUpdate(SeasonTicketRequestDTO seasonTicketRequestDTO, User userSecurity) throws NotFoundEntityException {
        Optional<com.senla.srs.model.User> optionalUser = userService.retrieveUserById(seasonTicketRequestDTO.getUserId());
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

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<SeasonTicket> optionalSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        //ToDo !!!!!!!!!!!!!!!!
//        optionalSeasonTicket.orElse(false).getAvailableForUse();

        if (optionalSeasonTicket.isPresent() && optionalSeasonTicket.get().getAvailableForUse()) {
                seasonTicketService.deleteById(id);
                return new ResponseEntity<>("Season ticket with this id was deleted", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(FORBIDDEN_FOR_DELETE, HttpStatus.FORBIDDEN);
        }
    }

    private boolean isThisUserSeasonTicket(Optional<SeasonTicket> optionalSeasonTicket,
                                           org.springframework.security.core.userdetails.User userSecurity) {

        return optionalSeasonTicket.isPresent() &&
                isThisUserById(userSecurity, optionalSeasonTicket.get().getUserId());
    }

    private boolean isValid(SeasonTicketRequestDTO seasonTicketRequestDTO,
                            Optional<com.senla.srs.model.User> optionalUser,
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
                                   Optional<com.senla.srs.model.User> optionalUser,
                                   Optional<ScooterType> optionalScooterType) throws NotFoundEntityException {

        int remainingTime = 0;

        if (optionalScooterType.isPresent()) {
            remainingTime = calculateRemainingTime(seasonTicketRequestDTO, optionalScooterType.get());
        }

        optionalUser.ifPresent(user -> user.setBalance(user.getBalance() - seasonTicketRequestDTO.getPrice()));

        ScooterType scooterType = scooterTypeService.retrieveScooterTypeById(seasonTicketRequestDTO.getScooterTypeId())
                .orElseThrow(() -> new NotFoundEntityException("ScooterType"));

        SeasonTicket seasonTicket = seasonTicketService.save(
                seasonTicketRequestMapper.toConsistencySeasonTicket(seasonTicketRequestDTO, scooterType, remainingTime, duration));

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
}
