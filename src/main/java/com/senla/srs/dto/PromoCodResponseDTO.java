package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PromoCodResponseDTO extends AbstractDto{
    @NonNull
    private String name;
    private RentalSessionResponseDTO rentalSession;
    @NonNull
    private LocalDate startDate;
    private LocalDate expiredDate;
    private Integer discountPercentage;
    private Integer bonusPoint;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromoCodResponseDTO)) return false;

        PromoCodResponseDTO that = (PromoCodResponseDTO) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
