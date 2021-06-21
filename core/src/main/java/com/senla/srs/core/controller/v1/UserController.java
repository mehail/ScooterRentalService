package com.senla.srs.core.controller.v1;

import com.senla.srs.core.controller.v1.facade.EntityControllerFacade;
import com.senla.srs.core.dto.user.UserDTO;
import com.senla.srs.core.dto.user.UserFullResponseDTO;
import com.senla.srs.core.dto.user.UserRequestDTO;
import com.senla.srs.core.exception.NotFoundEntityException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final EntityControllerFacade<UserDTO, UserRequestDTO, UserFullResponseDTO, Long> entityControllerFacade;

    @Operation(summary = "Get a list of Users",
            description = "If the User is not an Administrator, then a list with an authorized User is returned")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public Page<UserFullResponseDTO> getAll(Integer page,
                                            Integer size,
                                            @RequestParam(defaultValue = "id") String sort,
                                            @Parameter(hidden = true)
                                            @RequestHeader(name = "Authorization", required = false) String token) {

        return entityControllerFacade.getAll(page, size, sort, token);
    }


    @Operation(operationId = "getById", summary = "Get a User by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserFullResponseDTO.class)))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @Parameter(hidden = true)
                                     @RequestHeader(name = "Authorization", required = false) String token)
            throws NotFoundEntityException {

        return entityControllerFacade.getById(id, token);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update User",
            description = "If the User exists - then the fields are updated, if not - created new User")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserFullResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid UserRequestDTO userRequestDTO,
                                            BindingResult bindingResult,
                                            @Parameter(hidden = true)
                                            @RequestHeader(name = "Authorization", required = false) String token)
            throws NotFoundEntityException {

        return entityControllerFacade.createOrUpdate(userRequestDTO, bindingResult, token);
    }


    @Operation(operationId = "delete", summary = "Delete User")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User id")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) throws NotFoundEntityException {
        return entityControllerFacade.delete(id, null);
    }

}
