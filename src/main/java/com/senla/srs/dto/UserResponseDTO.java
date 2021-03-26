package com.senla.srs.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class UserResponseDTO extends UserDTO{
    @NonNull
    private Long id;
    @NonNull
    private Integer balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponseDTO)) return false;
        if (!super.equals(o)) return false;

        UserResponseDTO that = (UserResponseDTO) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
