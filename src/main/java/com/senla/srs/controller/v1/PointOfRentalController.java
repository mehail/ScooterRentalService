package com.senla.srs.controller.v1;

import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalResponseDTO;
import com.senla.srs.mapper.PointOfRentalRequestMapper;
import com.senla.srs.mapper.PointOfRentalResponseMapper;
import com.senla.srs.model.PointOfRental;
import com.senla.srs.service.AddressDtoService;
import com.senla.srs.service.PointOfRentalService;
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
@Tag(name = "Point of rental REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/point_of_rentals")
public class PointOfRentalController {
    private final AddressDtoService addressDtoService;
    private final PointOfRentalService pointOfRentalService;
    private final PointOfRentalRequestMapper pointOfRentalRequestMapper;
    private final PointOfRentalResponseMapper pointOfRentalResponseMapper;

    private static final String POINT_OF_RENTAL_NOT_FOUND = "A Point of rental with this id was not found";


    @Operation(summary = "Get a list of Point of rentals")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('pointOfRentals:read')")
    public Page<PointOfRentalResponseDTO> getAll(Integer page, Integer size, @RequestParam(defaultValue = "id") String sort) {
        return pointOfRentalResponseMapper.mapPageToDtoPage(pointOfRentalService.retrieveAllPointOfRentals(page, size, sort));
    }


    @Operation(operationId = "getById", summary = "Get a Point of rental by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Point of rental id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PointOfRentalResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = POINT_OF_RENTAL_NOT_FOUND)

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pointOfRentals:read')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<PointOfRental> optionalPointOfRental = pointOfRentalService.retrievePointOfRentalById(id);

        return optionalPointOfRental.isPresent()
                ? ResponseEntity.ok(pointOfRentalResponseMapper.toDto(optionalPointOfRental.get()))
                : new ResponseEntity<>(POINT_OF_RENTAL_NOT_FOUND, HttpStatus.NOT_FOUND);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update Point of rental",
            description = "If the Point of rental exists - then the fields are updated, if not - created new Point of rental")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PointOfRentalResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('pointOfRentals:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody PointOfRentalRequestDTO pointOfRentalRequestDTO) {
        if (addressDtoService.retrieveAddressDtoById(pointOfRentalRequestDTO.getAddressId()).isEmpty()) {
            return new ResponseEntity<>("The address is not correct", HttpStatus.FORBIDDEN);
        }

        PointOfRental pointOfRental = pointOfRentalService.save(pointOfRentalRequestMapper.toEntity(pointOfRentalRequestDTO));

        return ResponseEntity.ok(pointOfRentalResponseMapper.toDto(pointOfRental));
    }


    @Operation(operationId = "delete", summary = "Delete Point of rental")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Point of rental id")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = POINT_OF_RENTAL_NOT_FOUND)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pointOfRentals:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            pointOfRentalService.deleteById(id);
            return new ResponseEntity<>("Point of rental with this id was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), POINT_OF_RENTAL_NOT_FOUND);
            return new ResponseEntity<>(POINT_OF_RENTAL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
