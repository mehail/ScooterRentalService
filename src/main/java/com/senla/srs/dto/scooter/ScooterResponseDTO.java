package com.senla.srs.dto.scooter;

import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"type"}, callSuper = false)
public class ScooterResponseDTO extends ScooterDTO{
    @NonNull
    private ScooterTypeResponseDTO type;
}
