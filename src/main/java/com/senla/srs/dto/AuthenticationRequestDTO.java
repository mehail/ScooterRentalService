package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"}, callSuper = false)
public class AuthenticationRequestDTO extends AbstractDTO {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
