package com.senla.srs.dto.scooter;

import com.senla.srs.dto.rentalsession.RentalSessionCompactResponseDTO;
import com.senla.srs.dto.rentalsession.RentalSessionFullResponseDTO;
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
