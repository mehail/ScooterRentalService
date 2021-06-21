package com.senla.srs.core.controller.v1;

import com.senla.srs.core.dto.security.AuthenticationRequestDTO;
import com.senla.srs.core.entity.User;
import com.senla.srs.core.entity.UserStatus;
import com.senla.srs.core.entity.security.Role;
import com.senla.srs.core.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class AuthenticationControllerTest {

    private final static String EMAIL = "login@mail.com";
    private final static String PASSWORD = "password";
    private final User user = new User(null, EMAIL, PASSWORD, "Random", "Random", Role.USER,
            UserStatus.ACTIVE, 0, null);
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {

        if (userService.retrieveUserByEmail(EMAIL).isEmpty()) {
            userService.save(user);
        }

    }

    @Test
    void authenticateOk() {
        ResponseEntity<?> responseEntity =
                authenticationController.authenticate(new AuthenticationRequestDTO(EMAIL, PASSWORD));

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(),
                "Authentication failed with valid login and password");
    }

    @Test
    void authenticateForbidden() {
        ResponseEntity<?> responseEntity =
                authenticationController.authenticate(new AuthenticationRequestDTO(EMAIL, "otherPassword"));

        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode(),
                "Authentication completed successfully with invalid login password");
    }

    @AfterEach
    public void tearDown() {
        userService.retrieveUserByEmail(EMAIL)
                .ifPresent(user -> userService.deleteById(user.getId()));
    }

}