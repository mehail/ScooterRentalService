package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class PointOfRentalResponseDTO extends AbstractDto{
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String city;
    @NonNull
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointOfRentalResponseDTO)) return false;

        PointOfRentalResponseDTO that = (PointOfRentalResponseDTO) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
