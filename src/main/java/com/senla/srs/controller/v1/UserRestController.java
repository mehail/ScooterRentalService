package com.senla.srs.controller.v1;

import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.mapper.UserFullResponseMapper;
import com.senla.srs.model.User;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final UserService userService;
    private final UserFullResponseMapper userFullResponseMapper;
    private final UserRequestMapper userRequestMapper;

    private static final String USER_NOT_FOUND = "A user with this id not found";
    private static final String RE_AUTH = "To change this user reAuthorize";
    private static final String CHANGE_DEFAULT_FIELD = "To top up your balance, obtain administrator rights or " +
            "deactivate a profile, contact the administrator";

    @GetMapping
    @PreAuthorize("hasAuthority('users:readAll')")
    public List<UserFullResponseDTO> getAll() {
        return userService.retrieveAllUsers().stream()
                .map(userFullResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:readAll')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.retrieveUserById(id);

        return optionalUser.isPresent()
                ? ResponseEntity.ok(userFullResponseMapper.toDto(optionalUser.get()))
                : new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/this/")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        Optional<User> optionalUser = userService.retrieveUserByEmail(userSecurity.getUsername());

        return optionalUser.isPresent()
                ? ResponseEntity.ok(userFullResponseMapper.toDto(optionalUser.get()))
                : new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateUsers(@RequestBody UserRequestDTO userRequestDTO,
                                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        Optional<User> optionalExistUser = userService.retrieveUserByEmail(userRequestDTO.getEmail());

        if (userSecurity != null) {
            if (isAdmin(userSecurity)) {
                return create(userRequestDTO);
            } else {
                if (optionalExistUser.isEmpty()) {
                    return constrainCreate(userRequestDTO);
                } else {
                    if (isThis(userRequestDTO, userSecurity)) {
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

    private boolean isAdmin(org.springframework.security.core.userdetails.User userSecurity) {
        return userSecurity != null && userService.isAdmin(userSecurity);
    }

    private boolean isThis(UserRequestDTO userRequestDTO, org.springframework.security.core.userdetails.User userSecurity) {
        return userSecurity != null && userSecurity.getUsername().equals(userRequestDTO.getEmail());
    }

    private ResponseEntity<?> constrainCreate(UserRequestDTO userRequestDTO) {
        return isValidDtoToConstrainCreate(userRequestDTO)
                ? create(userRequestDTO)
                : new ResponseEntity<>(CHANGE_DEFAULT_FIELD, HttpStatus.FORBIDDEN);
    }

    private boolean isValidDtoToConstrainCreate(UserRequestDTO userRequestDTO) {
        return userRequestDTO.getStatus() == UserStatus.ACTIVE &&
                userRequestDTO.getRole() == Role.USER &&
                userRequestDTO.getBalance() == 0;
    }

    private ResponseEntity<?> update(UserRequestDTO userRequestDTO, User existUser) {
        return isValidDtoToUpdate(userRequestDTO, existUser)
                ? create(userRequestDTO)
                : new ResponseEntity<>(CHANGE_DEFAULT_FIELD, HttpStatus.FORBIDDEN);
    }

    private boolean isValidDtoToUpdate(UserRequestDTO userRequestDTO, User existUser) {
        return userRequestDTO.getStatus() == existUser.getStatus() &&
                userRequestDTO.getRole() == existUser.getRole() &&
                userRequestDTO.getBalance().equals(existUser.getBalance());
    }

    private ResponseEntity<?> create(UserRequestDTO userRequestDTO) {
        userService.save(userRequestMapper.toEntity(userRequestDTO));

        Optional<User> optionalUser = userService.retrieveUserByEmail(userRequestDTO.getEmail());

        return optionalUser.isPresent()
                ? ResponseEntity.ok(userFullResponseMapper.toDto(optionalUser.get()))
                : new ResponseEntity<>("The user is not created", HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>("User with this id was found", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), USER_NOT_FOUND);
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
    }
}
