package com.senla.srs.dto;

import com.senla.srs.model.ScooterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ScooterDTO extends AbstractDTO {
    @NonNull
    private String serialNumber;
    @NonNull
    private PointOfRentalDTO pointOfRental;
    @NonNull
    private ScooterTypeDTO type;
    @NonNull
    private ScooterStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScooterDTO)) return false;

        ScooterDTO that = (ScooterDTO) o;

        return getSerialNumber().equals(that.getSerialNumber());
    }

    @Override
    public int hashCode() {
        return getSerialNumber().hashCode();
    }
}
