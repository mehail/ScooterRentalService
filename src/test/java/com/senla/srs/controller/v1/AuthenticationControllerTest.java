package com.senla.srs.controller.v1;

import com.senla.srs.dto.security.AuthenticationRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController authenticationController;
    @Value("${auth.login.admin}")
    private String adminLogin;
    @Value("${auth.password.admin}")
    private String adminPassword;

    @Test
    void createAuthenticationController() {
        assertThat(authenticationController).isNotNull();
    }

    @Test
    void authenticateOk() {
        ResponseEntity<?> responseEntity =
                authenticationController.authenticate(new AuthenticationRequestDTO(adminLogin, adminPassword));

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(),
                "Authentication failed with valid login and password");
    }

    @Test
    void authenticateForbidden() {
        ResponseEntity<?> responseEntity =
                authenticationController.authenticate(new AuthenticationRequestDTO(adminLogin, "otherPassword"));

        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode(),
                "Authentication completed successfully with invalid login password");
    }
}