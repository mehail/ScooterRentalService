package com.senla.srs.controller.v1;

import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import com.senla.srs.mapper.ScooterTypeRequestMapper;
import com.senla.srs.mapper.ScooterTypeResponseMapper;
import com.senla.srs.model.ScooterType;
import com.senla.srs.service.MakerDtoService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Tag(name = "Scooter type REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/scooter_types")
public class ScooterTypeController {
    private final ScooterTypeService scooterTypeService;
    private final MakerDtoService makerDtoService;
    private final ScooterTypeRequestMapper scooterTypeRequestMapper;
    private final ScooterTypeResponseMapper scooterTypeResponseMapper;

    private static final String TYPE_NOT_FOUND = "Scooter type with this id not found";


    @Operation(summary = "Get a list of Scooter types")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('scooterTypes:read')")
    public Page<ScooterTypeResponseDTO> getAll(Integer page, Integer size, @RequestParam(defaultValue = "id") String sort) {
        return scooterTypeResponseMapper.mapPageToDtoPage(scooterTypeService.retrieveAllScooterTypes(page, size, sort));
    }


    @Operation(operationId = "getById", summary = "Get a Scooter type by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Scooter type id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ScooterTypeResponseDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = TYPE_NOT_FOUND)

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('scooterTypes:read')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<ScooterType> optionalScooterType = scooterTypeService.retrieveScooterTypeById(id);

        return optionalScooterType.isPresent()
                ? ResponseEntity.ok(scooterTypeResponseMapper.toDto(optionalScooterType.get()))
                : new ResponseEntity<>(TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update Scooter type",
            description = "If the Scooter type exists - then the fields are updated, if not - created new Scooter type")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ScooterTypeResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('scooterTypes:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody ScooterTypeRequestDTO scooterTypeRequestDTO) {

        if (makerDtoService.retrieveMakerDtoById(scooterTypeRequestDTO.getMakerId()).isEmpty()) {
            return new ResponseEntity<>("The maker is not correct", HttpStatus.FORBIDDEN);
        }

        ScooterType scooterType = scooterTypeService.save(scooterTypeRequestMapper.toEntity(scooterTypeRequestDTO));
        return ResponseEntity.ok(scooterTypeResponseMapper.toDto(scooterType));
    }


    @Operation(operationId = "delete", summary = "Delete Scooter type")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Scooter type id")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = TYPE_NOT_FOUND)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('scooterTypes:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            scooterTypeService.deleteById(id);
            return new ResponseEntity<>("Scooter type with this id was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), TYPE_NOT_FOUND);
            return new ResponseEntity<>(TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}

