package com.senla.srs.controllers;

import com.senla.srs.exception.ResourceNotFoundException;
import com.senla.srs.model.entity.User;
import com.senla.srs.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
public class UserController {
//    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //get users
    @GetMapping
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(user);
    }

    //create user
    @PostMapping
    public User createUser(@RequestBody() User user) {
        return userRepository.save(user);
    }

    //update user
    //ToDo read about annotation @Valid
    @PutMapping("/{id}")
    public ResponseEntity<User> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                   @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        //We do not change the role
        user.setLogin(userDetails.getLogin());
        user.setPassword(userDetails.getPassword());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setBirthday(userDetails.getBirthday());
        user.setEmail(userDetails.getEmail());
        //ToDo balance replenishment only for admin
        user.setBalance(userDetails.getBalance());

        return ResponseEntity.ok(this.userRepository.save(user));
    }

    //delete user
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User employee = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        userRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
