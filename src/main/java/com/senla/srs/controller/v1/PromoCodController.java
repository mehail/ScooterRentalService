package com.senla.srs.controller.v1;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.mapper.PromoCodMapper;
import com.senla.srs.model.PromoCod;
import com.senla.srs.service.PromoCodService;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/promo_codes")
public class PromoCodController extends AbstractRestController {
    private final PromoCodService promoCodService;
    private final PromoCodMapper promoCodMapper;

    private static final String NO_PROMO_COD_WITH_NAME = "No promo code with this name found";

    public PromoCodController(UserService userService,
                              PromoCodService promoCodService,
                              PromoCodMapper promoCodMapper) {
        super(userService);
        this.promoCodService = promoCodService;
        this.promoCodMapper = promoCodMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('promoCods:read')")
    public List<PromoCodDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        return isAdmin(userSecurity)

                ? promoCodService.retrieveAllPromoCods().stream()
                    .map(promoCodMapper::toDto)
                    .collect(Collectors.toList())

                : new ArrayList<>();
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('promoCods:read')")
    public ResponseEntity<?> getById(@PathVariable String name) {
        Optional<PromoCod> optionalPromoCod = promoCodService.retrievePromoCodByName(name);

        return optionalPromoCod.isPresent()
                ? ResponseEntity.ok(promoCodMapper.toDto(optionalPromoCod.get()))
                : new ResponseEntity<>(NO_PROMO_COD_WITH_NAME, HttpStatus.FORBIDDEN);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('promoCods:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody PromoCodDTO promoCodDTO) {
        return isValidDate(promoCodDTO)
                ? create(promoCodDTO)
                : new ResponseEntity<>("The start and end dates of the promo code are not correct", HttpStatus.FORBIDDEN);
    }

    private boolean isValidDate(PromoCodDTO promoCodDTO) {
        LocalDate startDate = promoCodDTO.getStartDate();
        LocalDate expiredDate = promoCodDTO.getExpiredDate();

        return expiredDate == null || startDate.isBefore(expiredDate);
    }

    private ResponseEntity<?> create(PromoCodDTO promoCodDTO) {
        PromoCod promoCod = promoCodService.save(promoCodMapper.toEntity(promoCodDTO));
        return ResponseEntity.ok(promoCodMapper.toDto(promoCod));
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('promoCods:write')")
    public ResponseEntity<?> delete(@PathVariable String name) {
        try {
            promoCodService.deleteById(name);
            return new ResponseEntity<>("Promo code with this serial number was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), NO_PROMO_COD_WITH_NAME);
            return new ResponseEntity<>(NO_PROMO_COD_WITH_NAME, HttpStatus.FORBIDDEN);
        }
    }
}
