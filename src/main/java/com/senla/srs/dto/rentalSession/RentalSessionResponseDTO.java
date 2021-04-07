package com.senla.srs.dto.rentalSession;

import com.senla.srs.dto.promoCod.PromoCodDTO;
import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.dto.seasonTicket.SeasonTicketRequestDTO;
import com.senla.srs.dto.user.UserResponseDTO;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionResponseDTO extends RentalSessionRequestDTO {

    public RentalSessionResponseDTO(@NonNull UserResponseDTO user, @NonNull ScooterDTO scooter, @NonNull LocalDate begin,
                                    LocalDate end, SeasonTicketRequestDTO seasonTicket, PromoCodDTO promoCod, @NonNull Long id, @NonNull Integer rate) {
        super(user, scooter, begin, end, seasonTicket, promoCod);
        this.id = id;
        this.rate = rate;
    }

    @NonNull
    private Long id;
    @NonNull
    private Integer rate;
}
