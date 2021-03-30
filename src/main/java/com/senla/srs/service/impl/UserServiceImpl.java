package com.senla.srs.service.impl;

import com.senla.srs.model.User;
import com.senla.srs.model.security.Role;
import com.senla.srs.repository.UserRepository;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> retrieveUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> retrieveUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserByAuthenticationPrincipal(org.springframework.security.core.userdetails.User userSecurity) {
        return retrieveUserByEmail(userSecurity.getUsername());
    }

    @Override
    public boolean isAdmin(org.springframework.security.core.userdetails.User userSecurity) {
        try {
            return getUserByAuthenticationPrincipal(userSecurity).get().getRole() == Role.ADMIN;
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), "A user with this id was not detected");
            return false;
        }
    }
}
