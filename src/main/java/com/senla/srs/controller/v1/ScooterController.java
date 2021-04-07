package com.senla.srs.controller.v1;

import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.mapper.ScooterMapper;
import com.senla.srs.model.Scooter;
import com.senla.srs.service.ScooterService;
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
@RequestMapping("/api/v1/scooters")
public class ScooterController {
    private ScooterService scooterService;
    private ScooterMapper scooterMapper;

    private static final String SCOOTER_NOT_FOUND = "A scooter with this serial number was not found";

    @GetMapping
    @PreAuthorize("hasAuthority('scooters:read')")
    public List<ScooterDTO> getAll() {
        return scooterService.retrieveAllScooters().stream()
                .map(scooter -> scooterMapper.toDto(scooter))
                .collect(Collectors.toList());
    }

    @GetMapping("/{serialNumber}")
    @PreAuthorize("hasAuthority('scooters:read')")
    public ResponseEntity<?> getById(@PathVariable String serialNumber) {
        Optional<Scooter> optionalScooter = scooterService.retrieveScooterBySerialNumber(serialNumber);

        return optionalScooter.isPresent()
                ? ResponseEntity.ok(scooterMapper.toDto(optionalScooter.get()))
                : new ResponseEntity<>(SCOOTER_NOT_FOUND, HttpStatus.FORBIDDEN);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody ScooterDTO scooterDTO) {
        scooterService.save(scooterMapper.toEntity(scooterDTO));
        Optional<Scooter> optionalScooter = scooterService.retrieveScooterBySerialNumber(scooterDTO.getSerialNumber());

        return optionalScooter.isPresent()
                ? ResponseEntity.ok(scooterMapper.toDto(optionalScooter.get()))
                : new ResponseEntity<>("The scooter is not created", HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{serialNumber}")
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> delete(@PathVariable String serialNumber) {
        try {
            scooterService.deleteById(serialNumber);
            return new ResponseEntity<>("Scooter with this serial number was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), SCOOTER_NOT_FOUND);
            return new ResponseEntity<>(SCOOTER_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
    }
}
