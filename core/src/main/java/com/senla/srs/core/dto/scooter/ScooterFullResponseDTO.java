package com.senla.srs.core.dto.scooter;

import com.senla.srs.core.dto.rentalsession.RentalSessionCompactResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"rentalSessions"}, callSuper = false)
public class ScooterFullResponseDTO extends ScooterCompactResponseDTO {

    @NonNull
    private List<RentalSessionCompactResponseDTO> rentalSessions;

}
