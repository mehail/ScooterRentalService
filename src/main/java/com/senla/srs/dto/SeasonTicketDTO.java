package com.senla.srs.dto;

import com.senla.srs.model.ScooterType;
import com.senla.srs.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SeasonTicketDTO {
    private Long id;
    @NonNull
    private User user;
    @NonNull
    private ScooterType scooterType;
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
        if (!(o instanceof SeasonTicketDTO)) return false;

        SeasonTicketDTO that = (SeasonTicketDTO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (!getUser().equals(that.getUser())) return false;
        if (!getScooterType().equals(that.getScooterType())) return false;
        return getPrice().equals(that.getPrice());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getUser().hashCode();
        result = 31 * result + getScooterType().hashCode();
        result = 31 * result + getPrice().hashCode();
        return result;
    }
}
