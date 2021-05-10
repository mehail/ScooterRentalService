package com.senla.srs.security;

import com.senla.srs.entity.security.Role;

public interface JwtTokenData {

    Long getId(String token);
    String getEmail(String token);
    Role getRole(String token);

}
