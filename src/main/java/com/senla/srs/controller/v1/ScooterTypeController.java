package com.senla.srs.controller.v1;

import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.mapper.ScooterTypeRequestMapper;
import com.senla.srs.mapper.ScooterTypeResponseMapper;
import com.senla.srs.model.ScooterType;
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
@RequestMapping("/api/v1/scooter_types")
public class ScooterTypeController {
    private ScooterTypeService scooterTypeService;
    private ScooterTypeRequestMapper scooterTypeRequestMapper;
    private ScooterTypeResponseMapper scooterTypeResponseMapper;

    private static final String TYPE_NOT_FOUND = "Scooter type with this id not found";

    @GetMapping
    @PreAuthorize("hasAuthority('scooterTypes:read')")
    public List<ScooterTypeRequestDTO> getAll() {
        return scooterTypeService.retrieveAllScooterTypes().stream()
                .map(scooterType -> scooterTypeResponseMapper.toDto(scooterType))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('scooterTypes:read')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<ScooterType> optionalScooterType = scooterTypeService.retrieveScooterTypeById(id);

        return optionalScooterType.isPresent()
                ? ResponseEntity.ok(scooterTypeResponseMapper.toDto(optionalScooterType.get()))
                : new ResponseEntity<>(TYPE_NOT_FOUND, HttpStatus.FORBIDDEN);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('scooterTypes:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody ScooterTypeRequestDTO scooterTypeRequestDTO) {
        scooterTypeService.save(scooterTypeRequestMapper.toEntity(scooterTypeRequestDTO));
        Optional<ScooterType> optionalScooterType =
                scooterTypeService.retrieveScooterTypeByModel(scooterTypeRequestDTO.getModel());

        return optionalScooterType.isPresent()
                ? ResponseEntity.ok(scooterTypeResponseMapper.toDto(optionalScooterType.get()))
                : new ResponseEntity<>("The scooter type is not created", HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('scooterTypes:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            scooterTypeService.deleteById(id);
            return new ResponseEntity<>("Scooter type with this id was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), TYPE_NOT_FOUND);
            return new ResponseEntity<>(TYPE_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
    }
}
