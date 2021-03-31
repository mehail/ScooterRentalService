package com.senla.srs.dto;

import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(of = {"password"}, callSuper = false)
public class UserRequestDTO extends UserDTO{
    @NonNull
    private String password;

    public UserRequestDTO(@NonNull String email, @NonNull String firstName, @NonNull String lastName,
                          @NonNull Role role, @NonNull UserStatus status, @NonNull Integer balance,
                          @NonNull String password) {
        super(email, firstName, lastName, role, status, balance);
        this.password = password;
    }
}
