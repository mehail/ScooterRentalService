package com.senla.srs.web.controller.v1.facade;

import com.senla.srs.core.dto.seasonticket.SeasonTicketDTO;
import com.senla.srs.core.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.core.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.core.entity.ScooterType;
import com.senla.srs.core.entity.SeasonTicket;
import com.senla.srs.core.entity.User;
import com.senla.srs.core.exception.NotFoundEntityException;
import com.senla.srs.core.mapper.SeasonTicketFullResponseMapper;
import com.senla.srs.core.mapper.SeasonTicketRequestMapper;
import com.senla.srs.core.security.JwtTokenData;
import com.senla.srs.core.service.ScooterTypeService;
import com.senla.srs.core.service.SeasonTicketService;
import com.senla.srs.core.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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
    private final int duration;
    private final Validator seasonTicketRequestValidator;

    public SeasonTicketControllerFacade(SeasonTicketService seasonTicketService,
                                        ScooterTypeService scooterTypeService,
                                        SeasonTicketRequestMapper seasonTicketRequestMapper,
                                        SeasonTicketFullResponseMapper seasonTicketFullResponseMapper,
                                        UserService userService,
                                        @Value("${srs.season.duration:365}") int duration,
                                        JwtTokenData jwtTokenData,
                                        @Qualifier("seasonTicketRequestValidator") Validator seasonTicketRequestValidator) {
        super(jwtTokenData);
        this.seasonTicketService = seasonTicketService;
        this.scooterTypeService = scooterTypeService;
        this.seasonTicketRequestMapper = seasonTicketRequestMapper;
        this.seasonTicketRequestValidator = seasonTicketRequestValidator;
        this.seasonTicketFullResponseMapper = seasonTicketFullResponseMapper;
        this.userService = userService;
        this.duration = duration;
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

        if (isAdmin(token) || isThisUserById(token, seasonTicketRequestDTO.getUserId())) {
            Optional<User> optionalUser = userService.retrieveUserById(seasonTicketRequestDTO.getUserId());
            Optional<ScooterType> optionalScooterType =
                    scooterTypeService.retrieveScooterTypeById(seasonTicketRequestDTO.getScooterTypeId());

            seasonTicketRequestValidator.validate(seasonTicketRequestDTO, bindingResult);

            return save(seasonTicketRequestDTO, optionalUser, optionalScooterType, bindingResult);
        } else {
            return new ResponseEntity<>("Creation of another user's season ticket requested", HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public ResponseEntity<?> delete(Long id, String token) throws NotFoundEntityException {
        Optional<SeasonTicket> optionalSeasonTicket = seasonTicketService.retrieveSeasonTicketsById(id);

        if (isCorrectDeleteSeasonTicket(id, token, optionalSeasonTicket)) {

            seasonTicketService.deleteById(id);

            return new ResponseEntity<>("Season ticket with this id was deleted", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(FORBIDDEN_FOR_DELETE, HttpStatus.FORBIDDEN);
        }

    }

    private boolean isCorrectDeleteSeasonTicket(Long id, String token, Optional<SeasonTicket> optionalSeasonTicket)
            throws NotFoundEntityException {

        boolean isHasRight = isThisUserById(token, optionalSeasonTicket
                .map(SeasonTicket::getUserId)
                .orElseThrow(() -> new NotFoundEntityException(SeasonTicket.class, id))) || isAdmin(token);

        boolean isAvailableForUse = optionalSeasonTicket
                .map(SeasonTicket::getAvailableForUse)
                .orElseThrow(() -> new NotFoundEntityException(SeasonTicket.class, id));

        return isHasRight && isAvailableForUse;
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

            int remainingTime = calculateRemainingTime(seasonTicketRequestDTO, optionalScooterType.get());
            int correctPrice = calculateCorrectPrice(optionalScooterType.get(), remainingTime);

            var seasonTicket = seasonTicketService.save(seasonTicketRequestMapper
                    .toEntity(seasonTicketRequestDTO,
                            optionalScooterType.get(),
                            correctPrice,
                            remainingTime,
                            duration));

            changeUserBalance(optionalUser, correctPrice);

            return new ResponseEntity<>(seasonTicketFullResponseMapper.toDto(seasonTicket), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }

    private void changeUserBalance(Optional<User> optionalUser, int price) {
        optionalUser.ifPresent(user -> user.setBalance(user.getBalance() - price));
    }

    private int calculateRemainingTime(SeasonTicketRequestDTO seasonTicketRequestDTO, ScooterType scooterType) {
        return seasonTicketRequestDTO.getPrice() / scooterType.getPricePerMinute();
    }

    private int calculateCorrectPrice(ScooterType scooterType, int remainingTime) {
        return scooterType.getPricePerMinute() * remainingTime;
    }

}
