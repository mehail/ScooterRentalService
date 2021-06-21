package com.senla.srs.core.service;

import com.senla.srs.core.entity.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

    User save(User user);

    Page<User> retrieveAllUsers(Integer pageNo, Integer pageSize, String sortBy);

    Page<User> retrieveAllUsersByEmail(String email, Integer pageNo, Integer pageSize, String sortBy);

    Optional<User> retrieveUserById(Long id);

    Optional<User> retrieveUserByEmail(String email);

    void deleteById(Long id);

}
