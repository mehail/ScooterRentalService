package com.senla.srs.dto.seasonticket;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "scooterType", "startDate"}, callSuper = false)
public class SeasonTicketRequestDTO extends AbstractDTO {
    @NonNull
    private Long userId;
    @NonNull
    private ScooterTypeRequestDTO scooterType;
    private LocalDate startDate;
}
