package com.senla.srs.dto.scooter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"typeId"}, callSuper = false)
public class ScooterRequestDTO extends ScooterDTO{
    @NonNull
    @Min(1)
    private Long typeId;
}
