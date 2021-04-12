package com.senla.srs.controller.v1;

import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalResponseDTO;
import com.senla.srs.mapper.PointOfRentalRequestMapper;
import com.senla.srs.mapper.PointOfRentalResponseMapper;
import com.senla.srs.model.PointOfRental;
import com.senla.srs.service.AddressDtoService;
import com.senla.srs.service.PointOfRentalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/point_of_rentals")
public class PointOfRentalController {
    private final AddressDtoService addressDtoService;
    private final PointOfRentalService pointOfRentalService;
    private final PointOfRentalRequestMapper pointOfRentalRequestMapper;
    private final PointOfRentalResponseMapper pointOfRentalResponseMapper;

    private static final String POINT_OF_RENTAL_NOT_FOUND = "A point of rental with this id was not found";

    @GetMapping
    @PreAuthorize("hasAuthority('pointOfRentals:read')")
    public List<PointOfRentalResponseDTO> getAll() {
        return pointOfRentalService.retrieveAllPointOfRentals().stream()
                .map(pointOfRentalResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pointOfRentals:read')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<PointOfRental> optionalPointOfRental = pointOfRentalService.retrievePointOfRentalById(id);

        return optionalPointOfRental.isPresent()
                ? ResponseEntity.ok(pointOfRentalResponseMapper.toDto(optionalPointOfRental.get()))
                : new ResponseEntity<>(POINT_OF_RENTAL_NOT_FOUND, HttpStatus.FORBIDDEN);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('pointOfRentals:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody PointOfRentalRequestDTO pointOfRentalRequestDTO) {
        if (addressDtoService.retrieveAddressDtoById(pointOfRentalRequestDTO.getAddressId()).isEmpty()) {
            return new ResponseEntity<>("The address is not correct", HttpStatus.FORBIDDEN);
        }

        pointOfRentalService.save(pointOfRentalRequestMapper.toEntity(pointOfRentalRequestDTO));

        Optional<PointOfRental> optionalPointOfRental =
                pointOfRentalService.retrievePointOfRentalByName(pointOfRentalRequestDTO.getName());

        return optionalPointOfRental.isPresent()
                ? ResponseEntity.ok(pointOfRentalResponseMapper.toDto(optionalPointOfRental.get()))
                : new ResponseEntity<>("The point of rental is not created", HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pointOfRentals:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            pointOfRentalService.deleteById(id);
            return new ResponseEntity<>("Point of rental with this id was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), POINT_OF_RENTAL_NOT_FOUND);
            return new ResponseEntity<>(POINT_OF_RENTAL_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
    }
}
