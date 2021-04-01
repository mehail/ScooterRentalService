package com.senla.srs.controller.v1;

import com.senla.srs.dto.UserDTO;
import com.senla.srs.dto.UserRequestDTO;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.mapper.UserResponseMapper;
import com.senla.srs.model.User;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    private UserService userService;
    private UserResponseMapper userResponseMapper;
    private UserRequestMapper userRequestMapper;
    private static final String USER_NOT_DETECTED = "A user with this id was not detected";
    private static final String RE_AUTH = "To change this user reAuthorize";
    private static final String CHANGE_DEFAULT_FIELD = "To top up your balance, obtain administrator rights or " +
            "deactivate a profile, contact the administrator";

    @GetMapping
    @PreAuthorize("hasAuthority('users:readAll')")
    public List<UserDTO> getAll() {
        return userService.retrieveAllUsers().stream()
                .map(user -> userResponseMapper.toDto(user))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:readAll')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            User user = userService.retrieveUserById(id).get();
            return ResponseEntity.ok(userResponseMapper.toDto(user));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), USER_NOT_DETECTED);
            return new ResponseEntity<>(USER_NOT_DETECTED, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/this/")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        try {
            User user = userService.retrieveUserByEmail(userSecurity.getUsername()).get();
            return ResponseEntity.ok(userResponseMapper.toDto(user));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), USER_NOT_DETECTED);
            return new ResponseEntity<>(USER_NOT_DETECTED, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateUsers(@RequestBody UserRequestDTO userRequestDTO,
                                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        Optional<User> optionalExistUser = userService.retrieveUserByEmail(userRequestDTO.getEmail());

        if (isAuth(userSecurity)) {
            if (isAdmin(userSecurity)) {
                return create(userRequestDTO);
            } else {
                if (!isExist(optionalExistUser)) {
                    return constrainCreate(userRequestDTO);
                } else {
                    if (isThis(userRequestDTO, userSecurity)) {
                        return update(userRequestDTO, optionalExistUser);
                    } else {
                        return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
                    }
                }
            }
        } else if (!isExist(optionalExistUser)) {
            return constrainCreate(userRequestDTO);
        } else {
            return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
        }
    }

    private boolean isAuth(org.springframework.security.core.userdetails.User userSecurity) {
        return userSecurity != null;
    }

    private boolean isAdmin(org.springframework.security.core.userdetails.User userSecurity) {
        return userSecurity != null && userService.isAdmin(userSecurity);
    }

    private boolean isExist(Optional<User> optionalExistUser) {
        return optionalExistUser.isPresent();
    }

    private boolean isThis(UserRequestDTO userRequestDTO, org.springframework.security.core.userdetails.User userSecurity) {
        return userSecurity != null && userSecurity.getUsername().equals(userRequestDTO.getEmail());
    }

    private ResponseEntity<?> constrainCreate(UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getStatus() == UserStatus.ACTIVE &&
                userRequestDTO.getRole() == Role.USER &&
                userRequestDTO.getBalance() == 0) {
            return create(userRequestDTO);
        } else {
            return new ResponseEntity<>(CHANGE_DEFAULT_FIELD, HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<?> update(UserRequestDTO userRequestDTO, Optional<User> optionalExistUser) {
        try {
            User existUser = optionalExistUser.get();
            if (userRequestDTO.getStatus() == existUser.getStatus() &&
                    userRequestDTO.getRole() == existUser.getRole() &&
                    userRequestDTO.getBalance().equals(existUser.getBalance())) {
                return create(userRequestDTO);
            } else {
                return new ResponseEntity<>(CHANGE_DEFAULT_FIELD, HttpStatus.FORBIDDEN);
            }
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), USER_NOT_DETECTED);
            return new ResponseEntity<>(USER_NOT_DETECTED, HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<?> create(UserRequestDTO userRequestDTO) {
        User user = userRequestMapper.toEntity(userRequestDTO);

        userService.save(user);

        try {
            User createdUser = userService.retrieveUserByEmail(userRequestDTO.getEmail()).get();
            return ResponseEntity.ok(userResponseMapper.toDto(createdUser));
        } catch (NoSuchElementException e) {
            String errorMessage = "The user is not created";
            log.error(e.getMessage(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>("User with this id was deleted", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), USER_NOT_DETECTED);
            return new ResponseEntity<>(USER_NOT_DETECTED, HttpStatus.FORBIDDEN);
        }
    }
}
