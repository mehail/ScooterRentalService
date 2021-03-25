package com.senla.srs.dto;

import com.senla.srs.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SeasonTicketResponseDTO extends AbstractDto{
    @NonNull
    private Long id;
    @NonNull
    private User user;
    @NonNull
    private ScooterTypeResponseDTO scooterType;
    @NonNull
    private Integer price;
    @NonNull
    private Integer remainingTime;
    private LocalDate startDate;
    private LocalDate expiredDate;
    @NonNull
    private Boolean availableForUse;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeasonTicketResponseDTO)) return false;

        SeasonTicketResponseDTO that = (SeasonTicketResponseDTO) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
