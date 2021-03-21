package com.senla.srs.controller;

import com.senla.srs.model.User;
import com.senla.srs.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {
    private UserService userService;

    public UserRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.retrieveAllUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.retrieveUserById(id).get();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User user = userService.retrieveUserById(id).get();
        userService.delete(user);
        return new ResponseEntity<>("Grohnuli", HttpStatus.ACCEPTED);
    }
}
