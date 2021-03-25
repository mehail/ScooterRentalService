package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class AuthenticationRequestResponseDTO extends AbstractDto{
    @NonNull
    private String email;
    @NonNull
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationRequestResponseDTO)) return false;

        AuthenticationRequestResponseDTO that = (AuthenticationRequestResponseDTO) o;

        return getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return getEmail().hashCode();
    }
}
