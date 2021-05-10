package com.senla.srs.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class UserCompactResponseDTO extends UserDTO {

    @NonNull
    private Long id;

}
