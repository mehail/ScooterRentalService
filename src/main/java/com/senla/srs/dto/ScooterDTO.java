package com.senla.srs.dto;

import com.senla.srs.model.ScooterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"serialNumber"}, callSuper = false)
public class ScooterDTO extends AbstractDTO {
    @NonNull
    private String serialNumber;
    @NonNull
    private PointOfRentalDTO pointOfRental;
    @NonNull
    private ScooterTypeDTO type;
    @NonNull
    private ScooterStatus status;
}
