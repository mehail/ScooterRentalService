package com.senla.srs.web.controller.v1;

import com.senla.srs.core.dto.seasonticket.SeasonTicketDTO;
import com.senla.srs.core.dto.seasonticket.SeasonTicketFullResponseDTO;
import com.senla.srs.core.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.core.exception.NotFoundEntityException;
import com.senla.srs.web.controller.v1.facade.EntityControllerFacade;
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

@Tag(name = "Season ticket REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/season_tickets")
public class SeasonTicketController {

    private final EntityControllerFacade<SeasonTicketDTO, SeasonTicketRequestDTO,
                SeasonTicketFullResponseDTO, Long> entityControllerFacade;

    @Operation(summary = "Get a list of Season tickets",
            description = "If the user is not an Administrator, then a list with an authorized user Season tickets is returned")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public Page<SeasonTicketFullResponseDTO> getAll(Integer page,
                                                    Integer size,
                                                    @RequestParam(defaultValue = "id") String sort,
                                                    @Parameter(hidden = true)
                                                    @RequestHeader(name = "Authorization", required = false) String token) {
        return entityControllerFacade.getAll(page, size, sort, token);
    }


    @Operation(operationId = "getById", summary = "Get a Season ticket by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Season ticket id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = SeasonTicketFullResponseDTO.class)))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @Parameter(hidden = true)
                                     @RequestHeader(name = "Authorization", required = false) String token)
            throws NotFoundEntityException {

        return entityControllerFacade.getById(id, token);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update Season ticket",
            description = "If the Season ticket exists - then the fields are updated, if not - created new Season ticket")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = SeasonTicketFullResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid SeasonTicketRequestDTO seasonTicketRequestDTO,
                                            BindingResult bindingResult,
                                            @Parameter(hidden = true)
                                            @RequestHeader(name = "Authorization", required = false) String token)
            throws NotFoundEntityException {

        return entityControllerFacade.createOrUpdate(seasonTicketRequestDTO, bindingResult, token);
    }


    @Operation(operationId = "delete", summary = "Delete Season ticket")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Season ticket id")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('seasonTickets:write')")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @Parameter(hidden = true)
                                    @RequestHeader(name = "Authorization", required = false) String token)
            throws NotFoundEntityException {

        return entityControllerFacade.delete(id, token);
    }

}
