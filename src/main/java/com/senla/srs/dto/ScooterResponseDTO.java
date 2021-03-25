package com.senla.srs.dto;

import com.senla.srs.model.ScooterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ScooterResponseDTO extends AbstractDto{
    @NonNull
    private String serialNumber;
    @NonNull
    private PointOfRentalResponseDTO pointOfRental;
    @NonNull
    private ScooterTypeResponseDTO type;
    @NonNull
    private ScooterStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScooterResponseDTO)) return false;

        ScooterResponseDTO that = (ScooterResponseDTO) o;

        return getSerialNumber().equals(that.getSerialNumber());
    }

    @Override
    public int hashCode() {
        return getSerialNumber().hashCode();
    }
}
