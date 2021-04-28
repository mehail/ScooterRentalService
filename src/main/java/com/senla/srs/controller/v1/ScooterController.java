package com.senla.srs.controller.v1;

import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.controller.v1.facade.EntityControllerFacade;
import com.senla.srs.exception.NotFoundEntityException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Scooter REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/scooters")
public class ScooterController {
    private final EntityControllerFacade<ScooterDTO, ScooterRequestDTO, ScooterResponseDTO, String> entityControllerFacade;

    @Operation(summary = "Get a list of Scooters")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('scooters:read')")
    public Page<ScooterResponseDTO> getAll(Integer page, Integer size, @RequestParam(defaultValue = "serialNumber") String sort) {
        return entityControllerFacade.getAll(page, size, sort, null);
    }


    @Operation(operationId = "getBySerialNumber", summary = "Get a Scooter by its serial number")
    @Parameter(in = ParameterIn.PATH, name = "serialNumber", description = "Scooter serial number")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ScooterResponseDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @GetMapping("/{serialNumber}")
    @PreAuthorize("hasAuthority('scooters:read')")
    public ResponseEntity<?> getBySerialNumber(@PathVariable String serialNumber) throws NotFoundEntityException {
        return entityControllerFacade.getById(serialNumber, null);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update Scooter",
            description = "If the Scooter exists - then the fields are updated, if not - created new Scooter")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ScooterResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody ScooterRequestDTO scooterDTO) throws NotFoundEntityException {
        return entityControllerFacade.createOrUpdate(scooterDTO, null);
    }


    @Operation(operationId = "delete", summary = "Delete Scooter")
    @Parameter(in = ParameterIn.PATH, name = "serialNumber", description = "Scooter serial number")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @DeleteMapping("/{serialNumber}")
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> delete(@PathVariable String serialNumber) {
        return entityControllerFacade.delete(serialNumber);
    }
}
