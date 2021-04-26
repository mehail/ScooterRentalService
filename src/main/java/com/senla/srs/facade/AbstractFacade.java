package com.senla.srs.facade;

import com.senla.srs.model.User;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Data
@AllArgsConstructor
@Controller
public abstract class AbstractFacade {
    protected final UserService userService;

    protected boolean isAdmin(org.springframework.security.core.userdetails.User userSecurity) {
        Optional<User> optionalUser = userService.retrieveUserByEmail(userSecurity.getUsername());

        return optionalUser.isPresent() && optionalUser.get().getRole() == Role.ADMIN;
    }

    protected boolean isThisUserById(org.springframework.security.core.userdetails.User userSecurity, Long id) {
        Optional<User> optionalUser = userService.retrieveUserByEmail(userSecurity.getUsername());

        return optionalUser.isPresent() && optionalUser.get().getId().equals(id);
    }

    protected boolean isThisUserByEmail(org.springframework.security.core.userdetails.User userSecurity, String email) {
        return userSecurity.getUsername().equals(email);
    }

    protected Long getAuthUserId(org.springframework.security.core.userdetails.User userSecurity) {
        return userService.retrieveUserByEmail(userSecurity.getUsername()).map(User::getId).orElse(0L);
    }
}
