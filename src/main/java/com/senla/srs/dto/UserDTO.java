package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AbstractDTO {
    @NonNull
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        if (!super.equals(o)) return false;

        UserDTO userDTO = (UserDTO) o;

        return getEmail().equals(userDTO.getEmail());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }
}
