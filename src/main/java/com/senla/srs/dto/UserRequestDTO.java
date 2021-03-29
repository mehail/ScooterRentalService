package com.senla.srs.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(of = {"password"}, callSuper = false)
public class UserRequestDTO extends UserDTO{
    @NonNull
    private String password;
    private String newPassword;
    private String newEmail;

    public UserRequestDTO(@NonNull String email, @NonNull String firstName, @NonNull String lastName, @NonNull String password) {
        super(email, firstName, lastName);
        this.password = password;
    }
}
