package com.senla.srs.core.dto.scooter;

import com.senla.srs.core.dto.scooter.type.ScooterTypeResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"type"}, callSuper = false)
public class ScooterCompactResponseDTO extends ScooterDTO{

    @NonNull
    private ScooterTypeResponseDTO type;

}
