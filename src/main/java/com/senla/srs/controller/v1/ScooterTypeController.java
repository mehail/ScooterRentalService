package com.senla.srs.controller.v1;

import com.senla.srs.dto.ScooterTypeDTO;
import com.senla.srs.mapper.ScooterTypeMapper;
import com.senla.srs.model.ScooterType;
import com.senla.srs.service.ScooterTypeService;
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
@RequestMapping("/api/v1/scooter_types")
public class ScooterTypeController {
    private ScooterTypeService scooterTypeService;
    private ScooterTypeMapper scooterTypeMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('scooterTypes:read')")
    public List<ScooterTypeDTO> getAll() {
        return scooterTypeService.retrieveAllScooterTypes().stream()
                .map(scooterType -> scooterTypeMapper.toDto(scooterType))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('scooterTypes:read')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ScooterType scooterType = scooterTypeService.retrieveScooterTypeById(id).get();
            return ResponseEntity.ok(scooterTypeMapper.toDto(scooterType));
        } catch (NoSuchElementException e) {
            String errorMessage = "No scooter type with this ID found";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('scooterTypes:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody ScooterTypeDTO scooterTypeDTO) {
        ScooterType scooterType = scooterTypeMapper.toEntity(scooterTypeDTO);
        scooterTypeService.save(scooterType);
        try {
            ScooterType createdScooterType = scooterTypeService.retrieveScooterTypeByModel(scooterTypeDTO.getModel()).get();
            return ResponseEntity.ok(scooterTypeMapper.toDto(createdScooterType));
        } catch (NoSuchElementException e) {
            String errorMessage = "The scooter type is not created";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('scooterTypes:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            ScooterType scooterType = scooterTypeService.retrieveScooterTypeById(id).get();
            scooterTypeService.deleteById(id);
            return new ResponseEntity<>("Scooter type with this id was deleted", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            String errorMessage = "A scooter type with this id was not detected";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }
}
