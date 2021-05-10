package com.senla.srs.service.impl;

import com.senla.srs.entity.User;
import com.senla.srs.repository.UserRepository;
import com.senla.srs.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final int encryptionStrength;

    public UserServiceImpl(UserRepository userRepository,
                           @Value("${jwt.encryption.strength}") int encryptionStrength) {
        this.userRepository = userRepository;
        this.encryptionStrength = encryptionStrength;
    }

    @Override
    public User save(User user) {
        user.setPassword(cryptPassword(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public Page<User> retrieveAllUsers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return userRepository.findAll(paging);
    }

    @Override
    public Page<User> retrieveAllUsersByEmail(String email, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return userRepository.findAllByEmail(email, paging);
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

    public String cryptPassword(String rowPassword) {
        var bCryptPasswordEncoder = new BCryptPasswordEncoder(encryptionStrength);

        return bCryptPasswordEncoder.encode(rowPassword);
    }

}
