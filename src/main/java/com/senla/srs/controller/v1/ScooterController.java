package com.senla.srs.controller.v1;

import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.mapper.ScooterRequestMapper;
import com.senla.srs.mapper.ScooterResponseMapper;
import com.senla.srs.model.Scooter;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.ScooterTypeService;
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
    private final ScooterService scooterService;
    private final ScooterTypeService scooterTypeService;
    private final ScooterRequestMapper scooterRequestMapper;
    private final ScooterResponseMapper scooterResponseMapper;

    private static final String SCOOTER_NOT_FOUND = "A scooter with this serial number was not found";

    @GetMapping
    @PreAuthorize("hasAuthority('scooters:read')")
    public List<ScooterResponseDTO> getAll() {
        return scooterService.retrieveAllScooters().stream()
                .map(scooterResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{serialNumber}")
    @PreAuthorize("hasAuthority('scooters:read')")
    public ResponseEntity<?> getById(@PathVariable String serialNumber) {
        Optional<Scooter> optionalScooter = scooterService.retrieveScooterBySerialNumber(serialNumber);

        return optionalScooter.isPresent()
                ? ResponseEntity.ok(scooterResponseMapper.toDto(optionalScooter.get()))
                : new ResponseEntity<>(SCOOTER_NOT_FOUND, HttpStatus.FORBIDDEN);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody ScooterRequestDTO scooterDTO) {
        if (scooterTypeService.retrieveScooterTypeById(scooterDTO.getTypeId()).isEmpty()) {
            return new ResponseEntity<>("The scooter type is not correct", HttpStatus.FORBIDDEN);
        }

        scooterService.save(scooterRequestMapper.toEntity(scooterDTO));

        Optional<Scooter> optionalScooter = scooterService.retrieveScooterBySerialNumber(scooterDTO.getSerialNumber());

        return optionalScooter.isPresent()
                ? ResponseEntity.ok(scooterResponseMapper.toDto(optionalScooter.get()))
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
