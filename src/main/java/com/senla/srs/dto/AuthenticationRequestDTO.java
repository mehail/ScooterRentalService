package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class AuthenticationRequestDTO extends AbstractDTO {
    @NonNull
    private String email;
    @NonNull
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationRequestDTO)) return false;

        AuthenticationRequestDTO that = (AuthenticationRequestDTO) o;

        return getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return getEmail().hashCode();
    }
}
