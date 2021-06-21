package com.senla.srs.core.dto.scooter;

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
    @Min(value = 1, message = "Type ID must be at least 1")
    private Long typeId;

}
