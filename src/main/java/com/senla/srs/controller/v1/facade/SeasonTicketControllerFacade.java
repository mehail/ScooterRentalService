package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.seasonticket.SeasonTicketDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.entity.ScooterType;
import com.senla.srs.entity.SeasonTicket;
import com.senla.srs.entity.User;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.SeasonTicketFullResponseMapper;
import com.senla.srs.mapper.SeasonTicketRequestMapper;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.ScooterTypeService;
import com.senla.srs.service.SeasonTicketService;
import com.senla.srs.service.UserService;
import com.senla.srs.validator.SeasonTicketRequestValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Controller
public class SeasonTicketControllerFacade extends AbstractFacade implements
        EntityControllerFacade<SeasonTicketDTO, SeasonTicketRequestDTO, SeasonTicketFullResponseDTO, Long> {

    private static final String FORBIDDEN_FOR_DELETE = "Season ticket with this id not available for deletion";
    private final SeasonTicketService seasonTicketService;
    private final ScooterTypeService scooterTypeService;
    private final UserService userService;
    private final SeasonTicketRequestMapper seasonTicketRequestMapper;
    private final SeasonTicketFullResponseMapper seasonTicketFullResponseMapper;
    private final SeasonTicketRequestValidator seasonTicketRequestValidator;
    private final int duration;


    public SeasonTicketControllerFacade(SeasonTicketService seasonTicketService,
                                        ScooterTypeService scooterTypeService,
                                        SeasonTicketRequestMapper seasonTicketRequestMapper,
                                        SeasonTicketFullResponseMapper seasonTicketFullResponseMapper,
                                        SeasonTicketRequestValidator seasonTicketRequestValidator,
                                        @Value("${srs.season.duration:365}") int duration,
                                        JwtTokenData jwtTokenData, UserService userService) {
        super(jwtTokenData);
        this.seasonTicketService = seasonTicketService;
        this.scooterTypeService = scooterTypeService;
        this.seasonTicketRequestMapper = seasonTicketRequestMapper;
        this.seasonTicketFullResponseMapper = seasonTicketFullResponseMapper;
        this.duration = duration;
        this.seasonTicketRequestValidator = seasonTicketRequestValidator;
        this.userService = userService;
    }

    @Override
    public Page<SeasonTicketFullResponseDTO> getAll(Integer page, Integer size, String sort, String token) {
        return isAdmin(token)
                ? seasonTicketFullResponseMapper.mapPageToDtoPage(seasonTicketService.retrieveAllSeasonTickets(page, size, sort))

                : seasonTicketFullResponseMapper.mapPageToDtoPage(
                seasonTicketService.retrieveAllSeasonTicketsByUserId(getAuthUserId(token), page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, String token) throws NotFoundEntityException {
        Optional<SeasonTicket> optionalSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (isAdmin(token) || isThisUserSeasonTicket(optionalSeasonTicket, token)) {
            return new ResponseEntity<>(optionalSeasonTicket
                    .map(seasonTicketFullResponseMapper::toDto)
                    .orElseThrow(() -> new NotFoundEntityException(SeasonTicket.class, id)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Another user's season ticket is requested", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> createOrUpdate(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                            BindingResult bindingResult,
                                            String token)
            throws NotFoundEntityException {

        Optional<SeasonTicket> optionalSeasonTicket =
                seasonTicketService.retrieveSeasonTicketByUserIdAndScooterTypeIdAndStartDate(seasonTicketRequestDTO.getUserId(),
                        seasonTicketRequestDTO.getScooterTypeId(),
                        seasonTicketRequestDTO.getStartDate());
        Optional<User> optionalUser = userService.retrieveUserById(seasonTicketRequestDTO.getUserId());
        Optional<ScooterType> optionalScooterType =
                scooterTypeService.retrieveScooterTypeById(seasonTicketRequestDTO.getScooterTypeId());

        SeasonTicketRequestDTO validSeasonTicketRequestDTO = seasonTicketRequestValidator.validate(seasonTicketRequestDTO,
                optionalSeasonTicket,
                optionalUser,
                optionalScooterType,
                bindingResult);

        return save(validSeasonTicketRequestDTO, optionalUser, optionalScooterType, bindingResult);
    }

    @Override
    public ResponseEntity<?> delete(Long id) throws NotFoundEntityException {
        Optional<SeasonTicket> optionalSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (optionalSeasonTicket
                .map(SeasonTicket::getAvailableForUse)
                .orElseThrow(() -> new NotFoundEntityException(SeasonTicket.class, id))) {

            seasonTicketService.deleteById(id);
            return new ResponseEntity<>("Season ticket with this id was deleted", HttpStatus.ACCEPTED);

        } else {
            return new ResponseEntity<>(FORBIDDEN_FOR_DELETE, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public Long getExistEntityId(SeasonTicketRequestDTO dto) {
        Optional<SeasonTicket> optionalExistSeasonTicket =
                seasonTicketService.retrieveSeasonTicketByUserIdAndScooterTypeIdAndStartDate(dto.getUserId(),
                        dto.getScooterTypeId(), dto.getStartDate());

        return optionalExistSeasonTicket
                .map(SeasonTicket::getId)
                .orElse(null);
    }

    private boolean isThisUserSeasonTicket(Optional<SeasonTicket> optionalSeasonTicket, String token) {
        return optionalSeasonTicket.isPresent() &&
                isThisUserById(token, optionalSeasonTicket.get().getUserId());
    }

    private ResponseEntity<?> save(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                   Optional<User> optionalUser,
                                   Optional<ScooterType> optionalScooterType,
                                   BindingResult bindingResult) {

        if (!bindingResult.hasErrors() && optionalScooterType.isPresent()) {

            changeUserBalance(optionalUser, seasonTicketRequestDTO);

            int remainingTime = calculateRemainingTime(seasonTicketRequestDTO, optionalScooterType.get());

            SeasonTicket seasonTicket = seasonTicketService.save(seasonTicketRequestMapper
                    .toEntity(seasonTicketRequestDTO,
                            optionalScooterType.get(),
                            calculateCorrectPrice(optionalScooterType.get(), remainingTime),
                            remainingTime,
                            duration));

            return new ResponseEntity<>(seasonTicketFullResponseMapper.toDto(seasonTicket), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }

    private void changeUserBalance(Optional<User> optionalUser, SeasonTicketRequestDTO seasonTicketRequestDTO) {
        optionalUser.ifPresent(user -> user.setBalance(user.getBalance() - seasonTicketRequestDTO.getPrice()));
    }

    private int calculateRemainingTime(SeasonTicketRequestDTO seasonTicketRequestDTO, ScooterType scooterType) {
        return seasonTicketRequestDTO.getPrice() / scooterType.getPricePerMinute();
    }

    private int calculateCorrectPrice(ScooterType scooterType, int remainingTime) {
        return scooterType.getPricePerMinute() * remainingTime;
    }
}
