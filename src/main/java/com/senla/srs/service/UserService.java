package com.senla.srs.service;

import com.senla.srs.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    User save(User user);
    List<User> retrieveAllUsers();
    Optional<User> retrieveUserById(Long id);
    Optional<User> retrieveUserByEmail(String email);
    void deleteById(Long id);
}
