package com.senla.srs.dto.scooter.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class ScooterTypeResponseDTO extends ScooterTypeRequestDTO {
    @NonNull
    private Long id;
}
