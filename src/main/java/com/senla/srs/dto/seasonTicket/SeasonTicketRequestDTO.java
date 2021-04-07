package com.senla.srs.dto.seasonTicket;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.dto.scooterType.ScooterTypeDTO;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "scooterType", "startDate"}, callSuper = false)
public class SeasonTicketRequestDTO extends AbstractDTO {
    @NonNull
    private Long userId;
    @NonNull
    private ScooterTypeDTO scooterType;
    private LocalDate startDate;
}
