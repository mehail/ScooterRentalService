package com.senla.srs.dto;

import com.senla.srs.model.ScooterStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"serialNumber"}, callSuper = false)
public class ScooterDTO extends AbstractDTO {
    @NonNull
    private String serialNumber;
    @NonNull
    private Long pointOfRentalId;
    @NonNull
    private ScooterTypeDTO type;
    @NonNull
    private ScooterStatus status;
}
