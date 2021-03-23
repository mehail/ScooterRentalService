package com.senla.srs.dto.test;

import com.senla.srs.model.RentalSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PromoCodDTO {
    @NonNull
    private String name;
    private RentalSession rentalSession;
    @NonNull
    private LocalDate startDate;
    private LocalDate expiredDate;
    private Integer discountPercentage;
    private Integer bonusPoint;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromoCodDTO)) return false;

        PromoCodDTO that = (PromoCodDTO) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
