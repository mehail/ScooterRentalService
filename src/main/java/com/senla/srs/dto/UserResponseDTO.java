package com.senla.srs.dto;

import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class UserResponseDTO extends UserDTO{
    @NonNull
    private Long id;
}
