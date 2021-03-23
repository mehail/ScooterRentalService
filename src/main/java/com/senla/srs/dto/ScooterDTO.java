package com.senla.srs.dto;

import com.senla.srs.model.PointOfRental;
import com.senla.srs.model.ScooterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ScooterDTO {
    @NonNull
    private String serialNumber;
    @NonNull
    private PointOfRental pointOfRental;
    @NonNull
    private ScooterType type;

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
