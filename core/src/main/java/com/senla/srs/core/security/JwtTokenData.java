package com.senla.srs.core.security;

import com.senla.srs.core.entity.security.Role;

public interface JwtTokenData {

    Long getId(String token);

    String getEmail(String token);

    Role getRole(String token);

}
