package com.senla.srs.dto.scooter.type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"makerId"}, callSuper = false)
public class ScooterTypeRequestDTO extends ScooterTypeDTO {
    @NonNull
    private Long makerId;
}
