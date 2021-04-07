package com.senla.srs.dto.user;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"email"}, callSuper = false)
public class UserDTO extends AbstractDTO {
    @NonNull
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private Role role;
    @NonNull
    private UserStatus status;
    @NonNull
    private Integer balance;
}
