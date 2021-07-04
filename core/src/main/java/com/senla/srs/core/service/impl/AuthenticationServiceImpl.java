package com.senla.srs.core.service.impl;

import com.senla.srs.core.dto.security.AuthenticationRequestDTO;
import com.senla.srs.core.dto.security.AuthenticationResponseDTO;
import com.senla.srs.core.security.JwtTokenProvider;
import com.senla.srs.core.service.AuthenticationService;
import com.senla.srs.core.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));

        var user = userService.retrieveUserByEmail(
                request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));

        String token = jwtTokenProvider.createToken(user.getId(), request.getEmail(), user.getRole().name());

        return new AuthenticationResponseDTO(user.getId(), user.getAccount().getEmail(), user.getRole(), token);
    }

}
