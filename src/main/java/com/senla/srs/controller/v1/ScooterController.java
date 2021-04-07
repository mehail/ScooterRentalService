package com.senla.srs.controller.v1;

import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.mapper.ScooterMapper;
import com.senla.srs.model.Scooter;
import com.senla.srs.service.ScooterService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/scooters")
public class ScooterController {
    private ScooterService scooterService;
    private ScooterMapper scooterMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('scooters:read')")
    public List<ScooterDTO> getAll() {
        return scooterService.retrieveAllScooters().stream()
                .map(scooter -> scooterMapper.toDto(scooter))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('scooters:read')")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Scooter scooter = scooterService.retrieveScooterBySerialNumber(id).get();
            return ResponseEntity.ok(scooterMapper.toDto(scooter));
        } catch (NoSuchElementException e) {
            String errorMessage = "No scooter with this serial number found";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody ScooterDTO scooterDTO) {
        Scooter scooter = scooterMapper.toEntity(scooterDTO);
        scooterService.save(scooter);
        try {
            Scooter createdScooter = scooterService.retrieveScooterBySerialNumber(scooter.getSerialNumber()).get();
            return ResponseEntity.ok(scooterMapper.toDto(createdScooter));
        } catch (NoSuchElementException e) {
            String errorMessage = "The scooter is not created";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{serialNumber}")
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> delete(@PathVariable String serialNumber) {
        try {
            scooterService.deleteById(serialNumber);
            return new ResponseEntity<>("Scooter with this serial number was deleted", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            String errorMessage = "A scooter with this serial number was not detected";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }
}
