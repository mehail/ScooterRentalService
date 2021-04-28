package com.senla.srs.controller.v1;

import com.senla.srs.model.User;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;

import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@Controller
public abstract class AbstractRestController {
    protected final UserService userService;

    protected boolean isAdmin(org.springframework.security.core.userdetails.User userSecurity) {
        Optional<User> optionalUser = userService.retrieveUserByEmail(userSecurity.getUsername());

        return optionalUser.map(User::getRole).orElse(null) == Role.ADMIN;
    }

    protected boolean isThisUserById(org.springframework.security.core.userdetails.User userSecurity, Long id) {
        Optional<User> optionalUser = userService.retrieveUserByEmail(userSecurity.getUsername());

        return Objects.equals(optionalUser.map(User::getId).orElse(null), id);
    }

    protected boolean isThisUserByEmail(org.springframework.security.core.userdetails.User userSecurity, String email) {
        return userSecurity.getUsername().equals(email);
    }

    protected Long getAuthUserId(org.springframework.security.core.userdetails.User userSecurity) {
        return userService.retrieveUserByEmail(userSecurity.getUsername()).map(User::getId).orElse(null);
    }
}
