package com.senla.srs.controller.v1;

import com.senla.srs.dto.pointofrental.PointOfRentalDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalResponseDTO;
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
@Tag(name = "Point of rental REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/point_of_rentals")
public class PointOfRentalController {
    private final EntityControllerFacade<PointOfRentalDTO, PointOfRentalRequestDTO,
            PointOfRentalResponseDTO, Long> entityControllerFacade;


    @Operation(summary = "Get a list of Point of rentals")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('pointOfRentals:read')")
    public Page<PointOfRentalResponseDTO> getAll(Integer page, Integer size, @RequestParam(defaultValue = "id") String sort) {
        return entityControllerFacade.getAll(page, size, sort, null);
    }


    @Operation(operationId = "getById", summary = "Get a Point of rental by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Point of rental id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PointOfRentalResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pointOfRentals:read')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return entityControllerFacade.getById(id, null);
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
    public ResponseEntity<?> createOrUpdate(@RequestBody PointOfRentalRequestDTO pointOfRentalRequestDTO)
            throws NotFoundEntityException {
        return entityControllerFacade.createOrUpdate(pointOfRentalRequestDTO, null);
    }


    @Operation(operationId = "delete", summary = "Delete Point of rental")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Point of rental id")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pointOfRentals:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
       return entityControllerFacade.delete(id);
    }
}
