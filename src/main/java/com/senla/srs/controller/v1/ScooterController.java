package com.senla.srs.controller.v1;

import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.mapper.ScooterRequestMapper;
import com.senla.srs.mapper.ScooterResponseMapper;
import com.senla.srs.model.Scooter;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.ScooterTypeService;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Scooter REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/scooters")
public class ScooterController {
    private final ScooterService scooterService;
    private final ScooterTypeService scooterTypeService;
    private final ScooterRequestMapper scooterRequestMapper;
    private final ScooterResponseMapper scooterResponseMapper;

    private static final String SCOOTER_NOT_FOUND = "A scooter with this serial number was not found";


    @Operation(summary = "Get a list of Scooters")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('scooters:read')")
    public List<ScooterResponseDTO> getAll() {
        return scooterService.retrieveAllScooters().stream()
                .map(scooterResponseMapper::toDto)
                .collect(Collectors.toList());
    }


    @Operation(operationId = "getBySerialNumber", summary = "Get a Scooter by its serial number")
    @Parameter(in = ParameterIn.PATH, name = "serialNumber", description = "Scooter serial number")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ScooterResponseDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = SCOOTER_NOT_FOUND)

    @GetMapping("/{serialNumber}")
    @PreAuthorize("hasAuthority('scooters:read')")
    public ResponseEntity<?> getBySerialNumber(@PathVariable String serialNumber) {
        Optional<Scooter> optionalScooter = scooterService.retrieveScooterBySerialNumber(serialNumber);

        return optionalScooter.isPresent()
                ? ResponseEntity.ok(scooterResponseMapper.toDto(optionalScooter.get()))
                : new ResponseEntity<>(SCOOTER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update Scooter",
            description = "If the Scooter exists - then the fields are updated, if not - created new Scooter")
    @Parameter(in = ParameterIn.PATH, name = "serialNumber", description = "Scooter serial number")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ScooterResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody ScooterRequestDTO scooterDTO) {
        if (scooterTypeService.retrieveScooterTypeById(scooterDTO.getTypeId()).isEmpty()) {
            return new ResponseEntity<>("The scooter type is not correct", HttpStatus.BAD_REQUEST);
        }

        Scooter scooter = scooterService.save(scooterRequestMapper.toEntity(scooterDTO));

        return ResponseEntity.ok(scooterResponseMapper.toDto(scooter));
    }


    @Operation(operationId = "delete", summary = "Delete Scooter")
    @Parameter(in = ParameterIn.PATH, name = "serialNumber", description = "Scooter serial number")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = SCOOTER_NOT_FOUND)

    @DeleteMapping("/{serialNumber}")
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> delete(@PathVariable String serialNumber) {
        try {
            scooterService.deleteById(serialNumber);
            return new ResponseEntity<>("Scooter with this serial number was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), SCOOTER_NOT_FOUND);
            return new ResponseEntity<>(SCOOTER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
