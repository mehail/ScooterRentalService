package com.senla.srs.dto;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class UserRequestDTO extends UserDTO{
    @NonNull
    private String password;

    public UserRequestDTO(@NonNull String email, @NonNull String firstName, @NonNull String lastName, @NonNull String password) {
        super(email, firstName, lastName);
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRequestDTO)) return false;
        if (!super.equals(o)) return false;

        UserRequestDTO that = (UserRequestDTO) o;

        return getPassword().equals(that.getPassword());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getPassword().hashCode();
        return result;
    }

}
