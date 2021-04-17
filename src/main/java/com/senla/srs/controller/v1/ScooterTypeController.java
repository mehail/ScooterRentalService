package com.senla.srs.controller.v1;

import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import com.senla.srs.mapper.ScooterTypeRequestMapper;
import com.senla.srs.mapper.ScooterTypeResponseMapper;
import com.senla.srs.model.ScooterType;
import com.senla.srs.service.MakerDtoService;
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
    private final ScooterTypeService scooterTypeService;
    private final MakerDtoService makerDtoService;
    private final ScooterTypeRequestMapper scooterTypeRequestMapper;
    private final ScooterTypeResponseMapper scooterTypeResponseMapper;

    private static final String TYPE_NOT_FOUND = "Scooter type with this id not found";

    @GetMapping
    @PreAuthorize("hasAuthority('scooterTypes:read')")
    public List<ScooterTypeResponseDTO> getAll() {
        return scooterTypeService.retrieveAllScooterTypes().stream()
                .map(scooterTypeResponseMapper::toDto)
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

        if (makerDtoService.retrieveMakerDtoById(scooterTypeRequestDTO.getMakerId()).isEmpty()) {
            return new ResponseEntity<>("The maker is not correct", HttpStatus.FORBIDDEN);
        }

        ScooterType scooterType = scooterTypeService.save(scooterTypeRequestMapper.toEntity(scooterTypeRequestDTO));
        return ResponseEntity.ok(scooterTypeResponseMapper.toDto(scooterType));
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
