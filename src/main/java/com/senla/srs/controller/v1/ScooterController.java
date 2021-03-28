package com.senla.srs.controller.v1;

import com.senla.srs.dto.ScooterDTO;
import com.senla.srs.mapper.ScooterMapper;
import com.senla.srs.model.Scooter;
import com.senla.srs.service.ScooterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
            Scooter scooter = scooterService.retrieveScooterById(id).get();
            ScooterDTO scooterDTO = scooterMapper.toDto(scooter);
            return ResponseEntity.ok(scooterDTO);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("No scooter with this ID found", HttpStatus.FORBIDDEN);
        }
    }

    //ToDo проверка на ошибку создания
    @PostMapping
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> create(@RequestBody ScooterDTO scooterDTO) {
        Scooter scooter = scooterMapper.toEntity(scooterDTO);
        scooterService.save(scooter);
        return ResponseEntity.ok(scooterDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('scooters:write')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            Scooter scooter = scooterService.retrieveScooterById(id).get();
            scooterService.deleteById(scooter);
            return new ResponseEntity<>("Scooter with this id was deleted", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("A scooter with this id was not detected", HttpStatus.FORBIDDEN);
        }
    }
}
