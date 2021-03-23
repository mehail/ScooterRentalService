package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class PointOfRentalDTO {
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
        if (!(o instanceof PointOfRentalDTO)) return false;

        PointOfRentalDTO that = (PointOfRentalDTO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (!getName().equals(that.getName())) return false;
        if (!getCity().equals(that.getCity())) return false;
        return getAddress().equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getName().hashCode();
        result = 31 * result + getCity().hashCode();
        result = 31 * result + getAddress().hashCode();
        return result;
    }
}
