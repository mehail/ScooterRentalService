package com.senla.srs.dto.scooter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"typeId"}, callSuper = false)
public class ScooterRequestDTO extends ScooterDTO{
    @NonNull
    private Long typeId;
}
