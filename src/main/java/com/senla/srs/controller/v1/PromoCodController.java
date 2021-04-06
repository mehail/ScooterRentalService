package com.senla.srs.controller.v1;

import com.senla.srs.dto.PromoCodDTO;
import com.senla.srs.mapper.PromoCodMapper;
import com.senla.srs.model.PromoCod;
import com.senla.srs.service.PromoCodService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/promo_codes")
public class PromoCodController {
    private PromoCodService promoCodService;
    private PromoCodMapper promoCodMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('promoCods:read')")
    public List<PromoCodDTO> getAll() {
        return promoCodService.retrieveAllPromoCods().stream()
                .map(promoCod -> promoCodMapper.toDto(promoCod))
                .collect(Collectors.toList());
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('promoCods:read')")
    public ResponseEntity<?> getById(@PathVariable String name) {
        try {
            PromoCod promoCod = promoCodService.retrievePromoCodByName(name).get();
            return ResponseEntity.ok(promoCodMapper.toDto(promoCod));
        } catch (NoSuchElementException e) {
            String errorMessage = "No promo code with this name found";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('promoCods:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody PromoCodDTO promoCodDTO) {
        if (isValidDate(promoCodDTO)) {
            return create(promoCodDTO);
        } else {
            return new ResponseEntity<>("The start and end dates of the promotional code are not correct",
                    HttpStatus.FORBIDDEN);
        }
    }

    private boolean isValidDate(PromoCodDTO promoCodDTO) {
        LocalDate startDate = promoCodDTO.getStartDate();
        LocalDate expiredDate = promoCodDTO.getExpiredDate();

        if (expiredDate == null) {
            return true;
        } else {
            return startDate.isBefore(expiredDate);
        }
    }

    private ResponseEntity<?> create(PromoCodDTO promoCodDTO) {
        PromoCod promoCod = promoCodMapper.toEntity(promoCodDTO);
        promoCodService.save(promoCod);
        try {
            PromoCod createdPromoCode = promoCodService.retrievePromoCodByName(promoCod.getName()).get();
            return ResponseEntity.ok(promoCodMapper.toDto(createdPromoCode));
        } catch (NoSuchElementException e) {
            String errorMessage = "The promo code is not created";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('promoCods:write')")
    public ResponseEntity<?> delete(@PathVariable String name) {
        try {
            promoCodService.deleteById(name);
            return new ResponseEntity<>("Promo code with this serial number was deleted", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            String errorMessage = "A promo code with this name was not detected";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }
}
