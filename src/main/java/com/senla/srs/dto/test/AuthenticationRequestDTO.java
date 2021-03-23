package com.senla.srs.dto.test;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequestDTO {
    private String email;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationRequestDTO)) return false;

        AuthenticationRequestDTO that = (AuthenticationRequestDTO) o;

        if (!getEmail().equals(that.getEmail())) return false;
        return getPassword().equals(that.getPassword());
    }

    @Override
    public int hashCode() {
        int result = getEmail().hashCode();
        result = 31 * result + getPassword().hashCode();
        return result;
    }
}
