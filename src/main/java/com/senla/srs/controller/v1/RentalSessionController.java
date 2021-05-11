package com.senla.srs.controller.v1;

import com.senla.srs.controller.v1.facade.EntityControllerFacade;
import com.senla.srs.dto.rentalsession.RentalSessionDTO;
import com.senla.srs.dto.rentalsession.RentalSessionFullResponseDTO;
import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.exception.NotFoundEntityException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Rental session REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/rental_sessions")
public class RentalSessionController {

    private final EntityControllerFacade<RentalSessionDTO, RentalSessionRequestDTO,
            RentalSessionFullResponseDTO, Long> entityControllerFacade;

    @Operation(summary = "Get a list of Rental sessions",
            description = "If the User is not an Administrator, then a list with an authorized User is returned")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public Page<RentalSessionFullResponseDTO> getAll(Integer page,
                                                     Integer size,
                                                     @RequestParam(defaultValue = "id") String sort,
                                                     @Parameter(hidden = true)
                                                 @RequestHeader(name = "Authorization", required = false) String token) {

        return entityControllerFacade.getAll(page, size, sort, token);
    }


    @Operation(operationId = "getById", summary = "Get a Rental session by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Rental session id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = RentalSessionFullResponseDTO.class)))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @Parameter(hidden = true)
                                     @RequestHeader(name = "Authorization", required = false) String token)
            throws NotFoundEntityException {

        return entityControllerFacade.getById(id, token);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update Rental session",
            description = "If the Rental session exists - then the fields are updated, if not - created new Rental session")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = RentalSessionRequestDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('rentalSessions:read')")
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid RentalSessionRequestDTO rentalSessionRequestDTO,
                                            BindingResult bindingResult,
                                            @Parameter(hidden = true)
                                            @RequestHeader(name = "Authorization", required = false) String token)
            throws NotFoundEntityException {

        return entityControllerFacade.createOrUpdate(rentalSessionRequestDTO, bindingResult, token);
    }


    @Operation(operationId = "delete", summary = "Delete Rental session")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Rental session id")
    @ApiResponse(responseCode = "202")
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('rentalSessions:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) throws NotFoundEntityException {

        return entityControllerFacade.delete(id, null);
    }

}

