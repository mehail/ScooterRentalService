package com.senla.srs.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"password"}, callSuper = false)
public class UserRequestDTO extends UserDTO {

    @NonNull
    @Length(min = 3, message = "Password must be at least 3 characters")
    private String password;

}
