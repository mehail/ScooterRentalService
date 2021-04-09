package com.senla.srs.dto.pointofrental;

import com.senla.srs.dto.scooter.ScooterDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class PointOfRentalResponseDTO extends PointOfRentalRequestDTO{
    private List<ScooterDTO> scooters;
    @NonNull
    private Long id;
}
