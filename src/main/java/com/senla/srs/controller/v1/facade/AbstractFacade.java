package com.senla.srs.controller.v1.facade;

import com.senla.srs.model.security.Role;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.UserService;
import org.springframework.stereotype.Controller;

@Controller
public abstract class AbstractFacade {
    protected final UserService userService;
    protected final JwtTokenData jwtTokenData;

    protected AbstractFacade(UserService userService, JwtTokenData jwtTokenData) {
        this.userService = userService;
        this.jwtTokenData = jwtTokenData;
    }

    protected boolean isAdmin(String token) {
        return jwtTokenData.getRole(token) == Role.ADMIN;
    }

    protected boolean isThisUserById(String token, Long id) {
        return jwtTokenData.getId(token).equals(id);
    }

    protected boolean isThisUserByEmail(String token, String email) {
        return jwtTokenData.getEmail(token).equals(email);
    }

    protected Long getAuthUserId(String token) {
        return jwtTokenData.getId(token);
    }

    protected String getAuthUserEmail(String token) {
        return jwtTokenData.getEmail(token);
    }

    protected Role getAuthUserRole(String token) {
        return jwtTokenData.getRole(token);
    }
}
