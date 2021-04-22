package com.senla.srs.controller.v1;

import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.mapper.UserFullResponseMapper;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.model.User;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Tag(name = "User REST Controller",
        description = "Interacting with user accounts")
@RestController
@RequestMapping("/api/v1/users")
class UserController extends AbstractRestController {
    private final UserFullResponseMapper userFullResponseMapper;
    private final UserRequestMapper userRequestMapper;

    private static final String USER_NOT_FOUND = "A User with this id not found";
    private static final String ACCESS_FORBIDDEN = "Access forbidden";
    private static final String RE_AUTH = "To change this User reAuthorize";
    private static final String CHANGE_DEFAULT_FIELD = "To top up your balance, obtain administrator rights or " +
            "deactivate a profile, contact the administrator";

    public UserController(UserService userService,
                          UserFullResponseMapper userFullResponseMapper,
                          UserRequestMapper userRequestMapper) {
        super(userService);
        this.userFullResponseMapper = userFullResponseMapper;
        this.userRequestMapper = userRequestMapper;
    }

    @Operation(summary = "Get a list of users",
            description = "If the user is not an Administrator, then a list with an authorized user is returned"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public List<UserFullResponseDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        return isAdmin(userSecurity)
                ? userFullResponseMapper.mapListToDtoList(userService.retrieveAllUsers())
                : userFullResponseMapper.mapListToDtoList(userService.retrieveUserByEmail(userSecurity.getUsername()).get());
    }


    @Operation(operationId = "getById", summary = "Get a User by its id")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User id")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserFullResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid User ID supplied")
    @ApiResponse(responseCode = "403", description = "Read access forbidden")
    @ApiResponse(responseCode = "404", description = USER_NOT_FOUND)

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        if (isThisUserById(userSecurity, id) || isAdmin(userSecurity)) {
            Optional<User> optionalUser = userService.retrieveUserById(id);

            return optionalUser.isPresent()
                    ? ResponseEntity.ok(userFullResponseMapper.toDto(optionalUser.get()))
                    : new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(ACCESS_FORBIDDEN, HttpStatus.FORBIDDEN);
        }
    }


    @Operation(operationId = "createOrUpdate", summary = "Create or update User",
            description = "If the User exists - then the fields are updated, if not - created new User")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User id")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserFullResponseDTO.class)))
    @ApiResponse(responseCode = "403", description = "Сreate/Update access forbidden")

    @PostMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody UserRequestDTO userRequestDTO,
                                            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        Optional<User> optionalExistUser = userService.retrieveUserByEmail(userRequestDTO.getEmail());

        if (userSecurity != null) {
            if (isAdmin(userSecurity)) {
                return save(userRequestDTO);
            } else {
                if (optionalExistUser.isEmpty()) {
                    return constrainCreate(userRequestDTO);
                } else {
                    if (isThisUserByEmail(userSecurity, userRequestDTO.getEmail())) {
                        return update(userRequestDTO, optionalExistUser.get());
                    } else {
                        return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
                    }
                }
            }
        } else if (optionalExistUser.isEmpty()) {
            return constrainCreate(userRequestDTO);
        } else {
            return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<?> constrainCreate(UserRequestDTO userRequestDTO) {
        return isValidDtoToConstrainCreate(userRequestDTO)
                ? save(userRequestDTO)
                : new ResponseEntity<>(CHANGE_DEFAULT_FIELD, HttpStatus.FORBIDDEN);
    }

    private boolean isValidDtoToConstrainCreate(UserRequestDTO userRequestDTO) {
        return userRequestDTO.getStatus() == UserStatus.ACTIVE &&
                userRequestDTO.getRole() == Role.USER &&
                userRequestDTO.getBalance() == 0;
    }

    private ResponseEntity<?> update(UserRequestDTO userRequestDTO, User existUser) {
        return isValidDtoToUpdate(userRequestDTO, existUser)
                ? save(userRequestDTO)
                : new ResponseEntity<>(CHANGE_DEFAULT_FIELD, HttpStatus.FORBIDDEN);
    }

    private boolean isValidDtoToUpdate(UserRequestDTO userRequestDTO, User existUser) {
        return userRequestDTO.getStatus() == existUser.getStatus() &&
                userRequestDTO.getRole() == existUser.getRole() &&
                userRequestDTO.getBalance().equals(existUser.getBalance());
    }

    private ResponseEntity<?> save(UserRequestDTO userRequestDTO) {
        User user = userService.save(userRequestMapper.toEntity(userRequestDTO));
        return ResponseEntity.ok(userFullResponseMapper.toDto(user));
    }


    @Operation(operationId = "delete", summary = "Delete User")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User id")
    @ApiResponse(responseCode = "202", description = "Accepted operation")
    @ApiResponse(responseCode = "404", description = USER_NOT_FOUND)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>("User with this id was found", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), USER_NOT_FOUND);
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
