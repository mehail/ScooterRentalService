package com.senla.srs.core.dto.security;

import com.senla.srs.core.dto.AbstractDTO;
import com.senla.srs.core.entity.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"}, callSuper = false)
public class AuthenticationResponseDTO extends AbstractDTO {

    @NonNull
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private Role role;
    @NonNull
    private String token;

}