package com.senla.srs.controller.v1;

import com.senla.srs.dto.PointOfRentalDTO;
import com.senla.srs.mapper.PointOfRentalMapper;
import com.senla.srs.model.PointOfRental;
import com.senla.srs.service.PointOfRentalService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/point_of_rentals")
public class PointOfRentalController {
    private static final String POINT_OF_RENTAL_NOT_DETECTED = "A point of rental with this id was not detected";
    private PointOfRentalService pointOfRentalService;
    private PointOfRentalMapper pointOfRentalMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('pointOfRentals:read')")
    public List<PointOfRentalDTO> getAll() {
        return pointOfRentalService.retrieveAllPointOfRentals().stream()
                .map(pointOfRental -> pointOfRentalMapper.toDto(pointOfRental))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pointOfRentals:read')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            PointOfRental pointOfRental = pointOfRentalService.retrievePointOfRentalById(id).get();
            return ResponseEntity.ok(pointOfRentalMapper.toDto(pointOfRental));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(POINT_OF_RENTAL_NOT_DETECTED, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('pointOfRentals:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody PointOfRentalDTO pointOfRentalRequestDTO) {
        PointOfRental pointOfRental = pointOfRentalMapper.toEntity(pointOfRentalRequestDTO);
        pointOfRentalService.save(pointOfRental);
        return ResponseEntity.ok(pointOfRentalMapper.toDto(pointOfRental));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pointOfRentals:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            pointOfRentalService.deleteById(id);
            return new ResponseEntity<>("Point of rental with this id was deleted", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(POINT_OF_RENTAL_NOT_DETECTED, HttpStatus.FORBIDDEN);
        }
    }
}
