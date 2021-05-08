package com.senla.srs.controller.v1.util;

import com.senla.srs.controller.v1.AuthenticationController;
import com.senla.srs.dto.security.AuthenticationRequestDTO;
import com.senla.srs.dto.security.AuthenticationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderImpl implements AuthProvider {
    @Value("${auth.login.admin}")
    private String adminLogin;
    @Value("${auth.password.admin}")
    private String adminPassword;
    @Value("${auth.login.user}")
    private String userLogin;
    @Value("${auth.password.user}")
    private String userPassword;
    @Value("${jwt.header}")
    private String headerName;

    @Autowired
    private AuthenticationController authenticationController;

    @Override
    public String getAdminToken() {
        return getToken(adminLogin, adminPassword);
    }

    @Override
    public String getUserToken() {
        return getToken(userLogin, userPassword);
    }

    @Override
    public HttpHeaders getResponseHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(headerName, token);
        return headers;
    }

    private String getToken(String login, String password) {
        AuthenticationResponseDTO authenticationResponseDTO = (AuthenticationResponseDTO) (authenticationController != null
                ? authenticationController.authenticate(new AuthenticationRequestDTO(login, password)).getBody()
                : null);

        return authenticationResponseDTO != null ? authenticationResponseDTO.getToken() : null;
    }
}
