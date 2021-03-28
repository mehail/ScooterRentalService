package com.senla.srs.controller.v1;

import com.senla.srs.dto.UserDTO;
import com.senla.srs.dto.UserRequestDTO;
import com.senla.srs.dto.UserResponseDTO;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.mapper.UserResponseMapper;
import com.senla.srs.model.User;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    private UserService userService;
    private UserResponseMapper userResponseMapper;
    private UserRequestMapper userRequestMapper;

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
            return new ResponseEntity<>("A user with this id was not detected", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/this/")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {
        try {
            User user = userService.retrieveUserByEmail(userSecurity.getUsername()).get();
            return ResponseEntity.ok(userResponseMapper.toDto(user));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("A user with this id was not detected", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserRequestDTO userRequestDTO,
                                    @AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {


        if (isAllowedEditUser(userRequestDTO, userSecurity)) {
            return new ResponseEntity<>("You are not allowed to edit this user", HttpStatus.FORBIDDEN);
        }

        User newUser = userRequestMapper.toEntity(userRequestDTO);
        userService.save(newUser);
        UserResponseDTO userResponseDTO = userResponseMapper.toDto(newUser);
        return ResponseEntity.ok(userResponseDTO);
    }

    private boolean isAllowedEditUser(UserRequestDTO userRequestDTO, org.springframework.security.core.userdetails.User userSecurity) {
        Optional<User> optionalAuthorizedUser = userService.retrieveUserByEmail(userSecurity.getUsername());
        boolean isExist = userService.retrieveUserByEmail(userRequestDTO.getEmail()).isPresent();
        boolean isAdmin = false;
        boolean isThisUser = false;

        if (optionalAuthorizedUser.isPresent()) {
            User authorizedUser = optionalAuthorizedUser.get();
            isAdmin = authorizedUser.getRole() == Role.ADMIN;
            isThisUser = authorizedUser.getEmail().equals(userRequestDTO.getEmail());
        }

        //ToDO разобраться с булевой логикой
        return isExist && (isAdmin || isThisUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>("User with this id was deleted", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("A user with this id was not detected", HttpStatus.FORBIDDEN);
        }
    }
}
