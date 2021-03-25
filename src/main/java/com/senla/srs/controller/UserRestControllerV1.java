package com.senla.srs.controller;

import com.senla.srs.dto.UserResponseDTO;
import com.senla.srs.mapper.UserMapper;
import com.senla.srs.model.User;
import com.senla.srs.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {
    private UserService userService;
    private UserMapper userMapper;

    public UserRestControllerV1(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.retrieveAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id) {
        User user = userService.retrieveUserById(id).get();
        System.out.println(user);
        return userMapper.toDto(user);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User user = userService.retrieveUserById(id).get();
        userService.delete(user);
        return new ResponseEntity<>("Grohnuli", HttpStatus.ACCEPTED);
    }
}
