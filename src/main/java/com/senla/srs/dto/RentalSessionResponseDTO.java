package com.senla.srs.dto;

import com.senla.srs.model.Scooter;
import com.senla.srs.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RentalSessionResponseDTO extends AbstractDto {
    @NonNull
    private Long id;
    @NonNull
    private User user;
    @NonNull
    private Scooter scooter;
    @NonNull
    private Integer rate;
    @NonNull
    private LocalDate begin;
    private LocalDate end;
    private SeasonTicketResponseDTO seasonTicket;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentalSessionResponseDTO)) return false;
        if (!super.equals(o)) return false;

        RentalSessionResponseDTO that = (RentalSessionResponseDTO) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
