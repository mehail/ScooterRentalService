package com.senla.srs.controller.v1;

import com.senla.srs.controller.v1.facade.EntityControllerFacade;
import com.senla.srs.dto.rentalsession.RentalSessionDTO;
import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.dto.rentalsession.RentalSessionResponseDTO;
import com.senla.srs.exception.NotFoundEntityException;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Tag(name = "Rental session REST Controller")
@RestController
@RequestMapping("/api/v1/rental_sessions")
public class RentalSessionController extends AbstractRestController {
    private final EntityControllerFacade<RentalSessionDTO, RentalSessionRequestDTO,
            RentalSessionResponseDTO, Long> entityControllerFacade;

    public RentalSessionController(UserService userService,
                                   EntityControllerFacade<RentalSessionDTO, RentalSessionRequestDTO,
                                           RentalSessionResponseDTO, Long> entityControllerFacade) {
        super(userService);
        this.entityControllerFacade = entityControllerFacade;
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

        return entityControllerFacade.getAll(page, size, sort, userSecurity);
    }


    @Operation(operationId = "getById", summary = "Get a Rental session by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Rental session id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = RentalSessionResponseDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity)
            throws NotFoundEntityException {

        return entityControllerFacade.getById(id, userSecurity);
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
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid RentalSessionRequestDTO rentalSessionRequestDTO,
                                            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity)
            throws NotFoundEntityException {

        return entityControllerFacade.createOrUpdate(rentalSessionRequestDTO, userSecurity);
    }


    @Operation(operationId = "delete", summary = "Delete Rental session")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Rental session id")
    @ApiResponse(responseCode = "202")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return entityControllerFacade.delete(id);
    }
}

