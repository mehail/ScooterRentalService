package com.senla.srs.controllers;

import com.senla.srs.exception.ResourceNotFoundException;
import com.senla.srs.model.entity.MyUser;
import com.senla.srs.model.repository.UserRepository1;
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
    private UserRepository1 userRepository1;

    @Autowired
    public UserController(UserRepository1 userRepository1) {
        this.userRepository1 = userRepository1;
    }

    //get users
    @GetMapping
    public List<MyUser> getAllUsers() {
        return this.userRepository1.findAll();
    }

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<MyUser> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        MyUser myUser = userRepository1.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(myUser);
    }

    //create user
    @PostMapping
    public MyUser createUser(@RequestBody() MyUser myUser) {
        return userRepository1.save(myUser);
    }

    //update user
    //ToDo read about annotation @Valid
    @PutMapping("/{id}")
    public ResponseEntity<MyUser> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                 @Valid @RequestBody MyUser myUserDetails) throws ResourceNotFoundException {
        MyUser myUser = userRepository1.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        //We do not change the role
        myUser.setLogin(myUserDetails.getLogin());
        myUser.setPassword(myUserDetails.getPassword());
        myUser.setFirstName(myUserDetails.getFirstName());
        myUser.setLastName(myUserDetails.getLastName());
        myUser.setBirthday(myUserDetails.getBirthday());
        myUser.setEmail(myUserDetails.getEmail());
        //ToDo balance replenishment only for admin
        myUser.setBalance(myUserDetails.getBalance());

        return ResponseEntity.ok(this.userRepository1.save(myUser));
    }

    //delete user
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        MyUser employee = userRepository1.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        userRepository1.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
