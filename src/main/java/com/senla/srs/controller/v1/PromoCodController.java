package com.senla.srs.controller.v1;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.controller.v1.facade.EntityControllerFacade;
import com.senla.srs.exception.NotFoundEntityException;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Tag(name = "PromoCod REST Controller")
@RestController
@RequestMapping("/api/v1/promo_codes")
public class PromoCodController extends AbstractRestController {
    private final EntityControllerFacade<PromoCodDTO, PromoCodDTO, PromoCodDTO, String> entityControllerFacade;

    public PromoCodController(UserService userService,
                              EntityControllerFacade<PromoCodDTO, PromoCodDTO, PromoCodDTO, String> entityControllerFacade) {
        super(userService);
        this.entityControllerFacade = entityControllerFacade;
    }


    @Operation(summary = "Get a list of PromoCods")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('promoCods:readAll')")
    public Page<PromoCodDTO> getAll(Integer page,
                                    Integer size,
                                    @RequestParam(defaultValue = "name") String sort) {
        return entityControllerFacade.getAll(page, size, sort, null);
    }


    @Operation(operationId = "getByName", summary = "Get a PromoCod by its name")
    @Parameter(in = ParameterIn.PATH, name = "name", description = "PromoCod name")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PromoCodDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('promoCods:read')")
    public ResponseEntity<?> getByName(@PathVariable String name) throws NotFoundEntityException {
        return entityControllerFacade.getById(name, null);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update PromoCod",
            description = "If the PromoCod exists - then the fields are updated, if not - created new PromoCod")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PromoCodDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('promoCods:write')")
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid PromoCodDTO promoCodDTO,
                                            BindingResult bindingResult)
            throws NotFoundEntityException {

        return entityControllerFacade.createOrUpdate(promoCodDTO, bindingResult, null);
    }


    @Operation(operationId = "delete", summary = "Delete PromoCod")
    @Parameter(in = ParameterIn.PATH, name = "name", description = "PromoCod name")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('promoCods:write')")
    public ResponseEntity<?> delete(@PathVariable String name) {
       return entityControllerFacade.delete(name);
    }
}
