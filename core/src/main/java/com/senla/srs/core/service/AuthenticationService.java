package com.senla.srs.core.service;

import com.senla.srs.core.dto.security.AuthenticationRequestDTO;
import com.senla.srs.core.dto.security.AuthenticationResponseDTO;

public interface AuthenticationService {

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

}
