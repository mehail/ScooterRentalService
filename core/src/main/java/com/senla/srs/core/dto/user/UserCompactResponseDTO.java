package com.senla.srs.core.dto.user;

import com.senla.srs.core.entity.Account;
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
