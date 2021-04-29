package com.senla.srs.dto.user;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import com.senla.srs.validator.EmailRFC822;
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
    @EmailRFC822
    @Length(max = 320)
    private String email;
    @NonNull
    @Length(min = 1, max = 64)
    private String firstName;
    @NonNull
    @Length(min = 1, max = 64)
    private String lastName;
    @NonNull
    private Role role;
    @NonNull
    private UserStatus status;
    @NonNull
    private Integer balance;
}
