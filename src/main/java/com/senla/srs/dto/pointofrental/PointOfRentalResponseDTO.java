package com.senla.srs.dto.pointofrental;

import com.senla.srs.dto.geo.CityDTO;
import com.senla.srs.dto.geo.GisPointDTO;
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
    private CityDTO cityDTO;
    private List<ScooterCompactResponseDTO> scooters;
    //ToDo Split into full and compact versions
    private List<GisPointDTO> rollingTrack;

}
