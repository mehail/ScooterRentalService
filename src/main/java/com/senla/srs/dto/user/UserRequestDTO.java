package com.senla.srs.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"password"}, callSuper = false)
public class UserRequestDTO extends UserDTO {
    @NonNull
    @Min(3)
    private String password;
}
