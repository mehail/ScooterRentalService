package com.senla.srs.service;

import com.senla.srs.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    User save(User user);
    Page<User> retrieveAllUsers(Integer pageNo, Integer pageSize, String sortBy);
    Page<User> retrieveAllUsersByEmail(String email, Integer pageNo, Integer pageSize, String sortBy);
    Optional<User> retrieveUserById(Long id);
    Optional<User> retrieveUserByEmail(String email);
    void deleteById(Long id);
}
