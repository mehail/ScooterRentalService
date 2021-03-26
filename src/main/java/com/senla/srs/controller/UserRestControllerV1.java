package com.senla.srs.controller;

import com.senla.srs.dto.UserDTO;
import com.senla.srs.dto.UserRequestDTO;
import com.senla.srs.dto.UserResponseDTO;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.mapper.UserResponseMapper;
import com.senla.srs.model.User;
import com.senla.srs.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {
    private UserService userService;
    private UserResponseMapper userResponseMapper;
    private UserRequestMapper userRequestMapper;

    public UserRestControllerV1(UserService userService, UserResponseMapper userResponseMapper, UserRequestMapper userRequestMapper) {
        this.userService = userService;
        this.userResponseMapper = userResponseMapper;
        this.userRequestMapper = userRequestMapper;
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.retrieveAllUsers().stream()
                .map(user -> userResponseMapper.toDto(user))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            User user = userService.retrieveUserById(id).get();
            UserResponseDTO userResponseDTO = userResponseMapper.toDto(user);
            return ResponseEntity.ok(userResponseDTO);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("A user with this id was not detected", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserRequestDTO userRequestDTO) {
        User user = userRequestMapper.toEntity(userRequestDTO);
        userService.save(user);
        UserResponseDTO userResponseDTO = userResponseMapper.toDto(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User user = userService.retrieveUserById(id).get();
        userService.delete(user);
        return new ResponseEntity<>("Grohnuli", HttpStatus.ACCEPTED);
    }
}
