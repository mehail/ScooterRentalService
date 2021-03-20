package com.senla.srs.model.service;

import com.senla.srs.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    //ToDo Мое творчество
    void save(User user);
    List<User> retrieveAllUsers();

    Optional<User> findByFirstName(String firstName);
}
