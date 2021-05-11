package com.senla.srs.dto.pointofrental;

import com.senla.srs.dto.db.AddressDTO;
import com.senla.srs.dto.scooter.ScooterCompactResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class PointOfRentalResponseDTO extends PointOfRentalDTO{

    @NonNull
    private Long id;
    @NonNull
    private AddressDTO address;
    private List<ScooterCompactResponseDTO> scooters;

}
