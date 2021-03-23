package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class UserDto extends AbstractDto {
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;

        UserDto userDTO = (UserDto) o;

        if (getId() != null ? !getId().equals(userDTO.getId()) : userDTO.getId() != null) return false;
        return getEmail().equals(userDTO.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getEmail().hashCode();
        return result;
    }
}
