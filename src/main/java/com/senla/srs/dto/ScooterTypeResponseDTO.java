package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ScooterTypeResponseDTO extends AbstractDto {
    @NonNull
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
        if (!(o instanceof ScooterTypeResponseDTO)) return false;
        if (!super.equals(o)) return false;

        ScooterTypeResponseDTO that = (ScooterTypeResponseDTO) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
