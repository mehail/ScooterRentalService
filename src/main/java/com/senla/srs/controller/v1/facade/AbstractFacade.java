package com.senla.srs.controller.v1.facade;

import com.senla.srs.entity.security.Role;
import com.senla.srs.security.JwtTokenData;
import org.springframework.stereotype.Controller;

@Controller
public abstract class AbstractFacade {

    protected final JwtTokenData jwtTokenData;

    protected AbstractFacade(JwtTokenData jwtTokenData) {
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
