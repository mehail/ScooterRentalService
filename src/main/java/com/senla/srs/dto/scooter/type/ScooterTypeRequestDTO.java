package com.senla.srs.dto.scooter.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"makerId"}, callSuper = false)
public class ScooterTypeRequestDTO extends ScooterTypeDTO {
    @NonNull
    @Min(1)
    private Long makerId;
}
