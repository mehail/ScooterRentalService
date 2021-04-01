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
    Optional<User> retrieveUserByEmail(String email);
    Optional<User> retrieveUserByAuthenticationPrincipal(org.springframework.security.core.userdetails.User userSecurity);
    void deleteById(Long id);
    boolean isAdmin(org.springframework.security.core.userdetails.User userSecurity);
    String cryptPassword(String rowPassword);
}
