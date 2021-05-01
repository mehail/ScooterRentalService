package com.senla.srs.dto.user;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"email"}, callSuper = false)
public class UserDTO extends AbstractDTO {
    @NonNull
    @Length(max = 320, message = "Email must be no more than 320 characters")
    private String email;
    @NonNull
    @Length(min = 1, max = 64, message = "First name must be between 1 and 64 characters")
    private String firstName;
    @NonNull
    @Length(min = 1, max = 64, message = "Last name must be between 1 and 64 characters")
    private String lastName;
    @NonNull
    private Role role;
    @NonNull
    private UserStatus status;
    @NonNull
    private Integer balance;
}
