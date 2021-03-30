package com.senla.srs.controller.v1;

import com.senla.srs.dto.UserDTO;
import com.senla.srs.dto.UserRequestDTO;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.mapper.UserResponseMapper;
import com.senla.srs.model.User;
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

        if (!isExistUser(userRequestDTO) || isAdmin(userSecurity) || isThisUser(userRequestDTO, userSecurity)) {
            return create(userRequestDTO);
        } else {
            return new ResponseEntity<>("Changing someone else's account is prohibited", HttpStatus.FORBIDDEN);
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

    private boolean isExistUser(UserRequestDTO userRequestDTO) {
        return userService.retrieveUserByEmail(userRequestDTO.getEmail()).isPresent();
    }

    private boolean isAdmin(org.springframework.security.core.userdetails.User userSecurity) {
        try {
            Optional<User> optionalAuthorizedUser = userService.retrieveUserByEmail(userSecurity.getUsername());
            User authorizedUser = optionalAuthorizedUser.get();
            return authorizedUser.getRole() == Role.ADMIN;
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), USER_NOT_DETECTED);
            return false;
        }
    }

    private boolean isThisUser(UserRequestDTO userRequestDTO, org.springframework.security.core.userdetails.User userSecurity) {
        try {
            Optional<User> optionalAuthorizedUser = userService.retrieveUserByEmail(userSecurity.getUsername());
            User authorizedUser = optionalAuthorizedUser.get();
            return authorizedUser.getEmail().equals(userRequestDTO.getEmail());
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), USER_NOT_DETECTED);
            return false;
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
