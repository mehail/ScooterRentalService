package com.senla.srs.dto.pointofrental;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.dto.db.AddressDTO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class PointOfRentalDTO extends AbstractDTO {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private AddressDTO address;
    @NonNull
    private Boolean available;
    private List<ScooterDTO> scooters;
}
