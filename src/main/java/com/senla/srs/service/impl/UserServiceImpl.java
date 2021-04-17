package com.senla.srs.service.impl;

import com.senla.srs.model.User;
import com.senla.srs.model.security.Role;
import com.senla.srs.repository.UserRepository;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final int encryptionStrength;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Value("${jwt.encryption.strength}") int encryptionStrength) {
        this.userRepository = userRepository;
        this.encryptionStrength = encryptionStrength;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
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
    public Optional<User> retrieveUserByAuthenticationPrincipal(org.springframework.security.core.userdetails.User userSecurity) {
        return retrieveUserByEmail(userSecurity.getUsername());
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
    public Long getAuthUserId(org.springframework.security.core.userdetails.User userSecurity) {
        return retrieveUserByAuthenticationPrincipal(userSecurity).map(User::getId).orElse(0L);
    }

    @Override
    public boolean isAdmin(org.springframework.security.core.userdetails.User userSecurity) {
        Optional<User> optionalUser = retrieveUserByAuthenticationPrincipal(userSecurity);

        return optionalUser.isPresent() && optionalUser.get().getRole() == Role.ADMIN;
    }

    @Override
    public boolean isThisUser(org.springframework.security.core.userdetails.User userSecurity, Long id) {
        return getAuthUserId(userSecurity).equals(id);
    }

    @Override
    public String cryptPassword(String rowPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(encryptionStrength);
        return bCryptPasswordEncoder.encode(rowPassword);
    }
}
