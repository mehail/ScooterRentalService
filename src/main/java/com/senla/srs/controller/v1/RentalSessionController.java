package com.senla.srs.controller.v1;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.mapper.RentalSessionRequestMapper;
import com.senla.srs.mapper.RentalSessionResponseMapper;
import com.senla.srs.model.*;
import com.senla.srs.service.RentalSessionService;
import com.senla.srs.service.RentalSessionValidator;
import com.senla.srs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Tag(name = "Rental session REST Controller")
@RestController
@RequestMapping("/api/v1/rental_sessions")
public class RentalSessionController extends AbstractRestController {
    private final RentalSessionService rentalSessionService;
    private final RentalSessionValidator rentalSessionValidator;
    private final RentalSessionRequestMapper rentalSessionRequestMapper;
    private final RentalSessionResponseMapper rentalSessionResponseMapper;

    private static final String RENTAL_SESSION_NOT_FOUND = "A rental session with this id was not found";

    public RentalSessionController(UserService userService,
                                   RentalSessionService rentalSessionService,
                                   RentalSessionValidator rentalSessionValidator,
                                   RentalSessionRequestMapper rentalSessionRequestMapper,
                                   RentalSessionResponseMapper rentalSessionResponseMapper) {
        super(userService);
        this.rentalSessionService = rentalSessionService;
        this.rentalSessionValidator = rentalSessionValidator;
        this.rentalSessionRequestMapper = rentalSessionRequestMapper;
        this.rentalSessionResponseMapper = rentalSessionResponseMapper;
    }


    @Operation(summary = "Get a list of Rental sessions",
            description = "If the User is not an Administrator, then a list with an authorized User is returned")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public Page<RentalSessionResponseDTO> getAll(Integer page, Integer size, @RequestParam(defaultValue = "id") String sort,
                                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        return isAdmin(userSecurity)
                ? rentalSessionResponseMapper.mapPageToDtoPage(rentalSessionService.retrieveAllRentalSessions(page, size, sort))

                : rentalSessionResponseMapper.mapPageToDtoPage(
                rentalSessionService.retrieveAllRentalSessionsByUserId(getAuthUserId(userSecurity), page, size, sort));
    }


    @Operation(operationId = "getById", summary = "Get a Rental session by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Rental session id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = RentalSessionResponseDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = RENTAL_SESSION_NOT_FOUND)

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> getById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                     @PathVariable Long id) {

        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (isAdmin(userSecurity) || isThisUserRentalSession(optionalRentalSession, userSecurity)) {

            return optionalRentalSession.isPresent()
                    ? ResponseEntity.ok(rentalSessionResponseMapper.toDto(optionalRentalSession.get()))
                    : new ResponseEntity<>(RENTAL_SESSION_NOT_FOUND, HttpStatus.NOT_FOUND);

        } else {
            return new ResponseEntity<>("Unauthorized user session requested", HttpStatus.FORBIDDEN);
        }
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update Rental session",
            description = "If the Rental session exists - then the fields are updated, if not - created new Rental session")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = RentalSessionRequestDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> createOrUpdate(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity,
                                            @RequestBody RentalSessionRequestDTO rentalSessionRequestDTO) {

        RentalSession rentalSession = rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO);

        return ResponseEntity.ok(rentalSessionResponseMapper.toDto(rentalSessionService.save(rentalSession)));

//        if (rentalSessionValidator.isValid(rentalSessionRequestDTO)) {
//            Optional<RentalSession> optionalRentalSession =
//                    rentalSessionService.retrieveRentalSessionByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(rentalSessionRequestDTO.getUserId(),
//                            rentalSessionRequestDTO.getScooterSerialNumber(),
//                            rentalSessionRequestDTO.getBeginDate(),
//                            rentalSessionRequestDTO.getBeginTime());
//            if (optionalRentalSession.isEmpty()) {
//                return save(rentalSessionRequestDTO);
//            } else if (optionalRentalSession.get().getEndDate() != null &&
//                    (isAdmin(userSecurity) || isThisUserById(userSecurity, rentalSessionRequestDTO.getUserId()))) {
//                return save(rentalSessionRequestDTO);
//            } else {
//                return new ResponseEntity<>("Completed rental session is not available for editing", HttpStatus.FORBIDDEN);
//            }
//        } else {
//            return new ResponseEntity<>("Rental session is not valid", HttpStatus.FORBIDDEN);
//        }
    }


    @Operation(operationId = "delete", summary = "Delete Rental session")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Rental session id")
    @ApiResponse(responseCode = "202")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = RENTAL_SESSION_NOT_FOUND)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<RentalSession> optionalRentalSession = rentalSessionService.retrieveRentalSessionById(id);

        if (optionalRentalSession.isPresent()) {
            if (optionalRentalSession.get().getEndDate() == null) {
                try {
                    rentalSessionService.deleteById(id);
                    return new ResponseEntity<>("Rental session with this id was deleted", HttpStatus.ACCEPTED);
                } catch (EmptyResultDataAccessException e) {
                    log.error(e.getMessage(), RENTAL_SESSION_NOT_FOUND);
                    return new ResponseEntity<>(RENTAL_SESSION_NOT_FOUND, HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Rental session closed and cannot be deleted", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Rental session with this id not available for deletion",
                    HttpStatus.FORBIDDEN);
        }
    }

    private boolean isThisUserRentalSession(Optional<RentalSession> optionalRentalSession,
                                            org.springframework.security.core.userdetails.User userSecurity) {

        return optionalRentalSession.isPresent() &&
                isThisUserById(userSecurity, optionalRentalSession.get().getUser().getId());
    }

    private ResponseEntity<?> save(RentalSessionRequestDTO rentalSessionRequestDTO) {
        RentalSession rentalSession = rentalSessionRequestMapper.toEntity(rentalSessionRequestDTO);

        changeEntityState(rentalSession);

        RentalSession createdRentalSession = rentalSessionService.save(rentalSession);

        return ResponseEntity.ok(rentalSessionResponseMapper.toDto(createdRentalSession));
    }

    private void changeEntityState(RentalSession rentalSession) {
        Scooter scooter = rentalSession.getScooter();
        SeasonTicket seasonTicket = rentalSession.getSeasonTicket();
        User user = rentalSession.getUser();

        if (rentalSession.getEndDate() == null) {
            scooter.setStatus(ScooterStatus.USED);

            if (seasonTicket != null) {
                seasonTicket.setAvailableForUse(false);
            }

        } else {
            user.setBalance(user.getBalance() - calculateRate(rentalSession));

            scooter.setStatus(ScooterStatus.AVAILABLE);

            int usageTime = (int) (Duration.between(rentalSession.getBeginDate(), rentalSession.getEndDate()).getSeconds() / 60);
            scooter.setTimeMillage(scooter.getTimeMillage() + usageTime);

            if (seasonTicket != null && seasonTicket.getRemainingTime() > 0) {
                seasonTicket.setAvailableForUse(true);
            }

        }
    }

    private int calculateRate(RentalSession rentalSession) {
        LocalDateTime begin = LocalDateTime.of(rentalSession.getBeginDate(), rentalSession.getBeginTime());
        LocalDateTime end = LocalDateTime.of(rentalSession.getEndDate(), rentalSession.getBeginTime());
        int usageTime = (int) (Duration.between(begin, end).getSeconds() / 60);

        int pricePerMinute = rentalSession.getScooter().getType().getPricePerMinute();

        int billableTime = calculateBillableTime(rentalSession.getSeasonTicket(), usageTime);

        int priceWithoutPromoCod = billableTime * pricePerMinute;

        return applyPromoCod(priceWithoutPromoCod, rentalSession.getPromoCod(), rentalSession.getUser());
    }

    private int calculateBillableTime(SeasonTicket seasonTicket, int usageTime) {
        if (seasonTicket != null) {

            if (seasonTicket.getRemainingTime() >= usageTime) {
                seasonTicket.setRemainingTime(seasonTicket.getRemainingTime() - usageTime);
                usageTime = 0;
            } else {
                usageTime -= seasonTicket.getRemainingTime();
                seasonTicket.setRemainingTime(0);
            }

            if (seasonTicket.getRemainingTime() == 0) {
                seasonTicket.setAvailableForUse(false);
            }

        }

        return usageTime;
    }

    private int applyPromoCod(int rate, PromoCod promoCod, User user) {
        int discountPercentage = 0;

        if (promoCod != null) {

            if (promoCod.getBonusPoint() > 0) {
                user.setBalance(user.getBalance() + promoCod.getBonusPoint());
            }

            if (promoCod.getDiscountPercentage() > 0) {
                discountPercentage = promoCod.getDiscountPercentage();
            }

            promoCod.setAvailable(false);
        }

        return rate * (1 - discountPercentage / 100);
    }
}

