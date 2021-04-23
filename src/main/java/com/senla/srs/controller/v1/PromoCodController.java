package com.senla.srs.controller.v1;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.mapper.PromoCodMapper;
import com.senla.srs.model.PromoCod;
import com.senla.srs.service.PromoCodService;
import com.senla.srs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "PromoCod REST Controller")
@RestController
@RequestMapping("/api/v1/promo_codes")
public class PromoCodController extends AbstractRestController {
    private final PromoCodService promoCodService;
    private final PromoCodMapper promoCodMapper;

    private static final String PROMO_COD_NOT_FOUND = "No PromoCod with this name found";

    public PromoCodController(UserService userService,
                              PromoCodService promoCodService,
                              PromoCodMapper promoCodMapper) {
        super(userService);
        this.promoCodService = promoCodService;
        this.promoCodMapper = promoCodMapper;
    }


    @Operation(summary = "Get a list of PromoCods")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('promoCods:readAll')")
    public List<PromoCodDTO> getAll() {
        return promoCodService.retrieveAllPromoCods().stream()
                .map(promoCodMapper::toDto)
                .collect(Collectors.toList());
    }


    @Operation(operationId = "getByName", summary = "Get a PromoCod by its name")
    @Parameter(in = ParameterIn.PATH, name = "name", description = "PromoCod name")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PromoCodDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = PROMO_COD_NOT_FOUND)

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('promoCods:read')")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        Optional<PromoCod> optionalPromoCod = promoCodService.retrievePromoCodByName(name);

        return optionalPromoCod.isPresent()
                ? ResponseEntity.ok(promoCodMapper.toDto(optionalPromoCod.get()))
                : new ResponseEntity<>(PROMO_COD_NOT_FOUND, HttpStatus.NOT_FOUND);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update PromoCod",
            description = "If the PromoCod exists - then the fields are updated, if not - created new PromoCod")
    @Parameter(in = ParameterIn.PATH, name = "name", description = "PromoCod name")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PromoCodDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('promoCods:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody PromoCodDTO promoCodDTO) {
        return isValidDate(promoCodDTO)
                ? create(promoCodDTO)
                : new ResponseEntity<>("The start and end dates of the PromoCod are not correct", HttpStatus.BAD_REQUEST);
    }


    @Operation(operationId = "delete", summary = "Delete PromoCod")
    @Parameter(in = ParameterIn.PATH, name = "name", description = "PromoCod name")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"),
            description = PROMO_COD_NOT_FOUND)

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('promoCods:write')")
    public ResponseEntity<?> delete(@PathVariable String name) {
        try {
            promoCodService.deleteById(name);
            return new ResponseEntity<>("PromoCod with this serial number was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), PROMO_COD_NOT_FOUND);
            return new ResponseEntity<>(PROMO_COD_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
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
}
