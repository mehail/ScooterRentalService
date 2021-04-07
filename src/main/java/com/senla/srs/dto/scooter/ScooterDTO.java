package com.senla.srs.dto.scooter;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.model.ScooterStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"serialNumber"}, callSuper = false)
public class ScooterDTO extends AbstractDTO {
    @NonNull
    private String serialNumber;
    @NonNull
    private Long pointOfRentalId;
    @NonNull
    private ScooterTypeRequestDTO type;
    @NonNull
    private ScooterStatus status;
}
