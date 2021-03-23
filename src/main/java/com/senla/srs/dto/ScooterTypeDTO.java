package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ScooterTypeDTO {
    private Long id;
    @NonNull
    private String model;
    @NonNull
    private String maker;
    @NonNull
    private Double maxSpeed;
    @NonNull
    private Integer pricePerMinute;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScooterTypeDTO)) return false;

        ScooterTypeDTO that = (ScooterTypeDTO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (!getModel().equals(that.getModel())) return false;
        return getMaker().equals(that.getMaker());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getModel().hashCode();
        result = 31 * result + getMaker().hashCode();
        return result;
    }
}
