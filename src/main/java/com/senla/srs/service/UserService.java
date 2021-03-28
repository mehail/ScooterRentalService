package com.senla.srs.service;

import com.senla.srs.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    void save(User user);
    List<User> retrieveAllUsers();
    Optional<User> retrieveUserById(Long id);
    void deleteById(Long id);
}
