package com.senla.srs.controller.v1;

import com.senla.srs.controller.v1.facade.EntityControllerFacade;
import com.senla.srs.dto.user.UserDTO;
import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.security.JwtTokenData;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Tag(name = "User REST Controller")
@RestController
@RequestMapping("/api/v1/users")
class UserController extends AbstractRestController {
    private final JwtTokenData jwtTokenData;
    private final EntityControllerFacade<UserDTO, UserRequestDTO, UserFullResponseDTO, Long> entityControllerFacade;

    public UserController(UserService userService,
                          JwtTokenData jwtTokenData,
                          EntityControllerFacade<UserDTO, UserRequestDTO, UserFullResponseDTO, Long> entityControllerFacade) {
        super(userService);
        this.jwtTokenData = jwtTokenData;
        this.entityControllerFacade = entityControllerFacade;
    }


    @Operation(summary = "Get a list of Users",
            description = "If the User is not an Administrator, then a list with an authorized User is returned")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @PageableAsQueryParam()

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public Page<UserFullResponseDTO> getAll(Integer page,
                                            Integer size,
                                            @RequestParam(defaultValue = "id") String sort,
                                            @AuthenticationPrincipal User userSecurity,
                                            @Parameter(hidden = true)
                                                @RequestHeader (name="Authorization", required = false)  String token) {

        return entityControllerFacade.getAll(page, size, sort, userSecurity);
    }


    @Operation(operationId = "getById", summary = "Get a User by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserFullResponseDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<?> getById(@PathVariable Long id, @AuthenticationPrincipal User userSecurity)
            throws NotFoundEntityException {

        return entityControllerFacade.getById(id, userSecurity);
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update User",
            description = "If the User exists - then the fields are updated, if not - created new User")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserFullResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid UserRequestDTO userRequestDTO,
                                            BindingResult bindingResult,
                                            @AuthenticationPrincipal User userSecurity)
            throws NotFoundEntityException {

        return entityControllerFacade.createOrUpdate(userRequestDTO, bindingResult, userSecurity);
    }


    @Operation(operationId = "delete", summary = "Delete User")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User id")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json"))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return entityControllerFacade.delete(id);
    }
}
