package com.senla.srs.dto.scooter;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import com.senla.srs.model.ScooterStatus;
import lombok.*;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"serialNumber"}, callSuper = false)
public class ScooterDTO extends AbstractDTO {
    @NonNull
    private String serialNumber;
    @NonNull
    private Long pointOfRentalId;
    @NonNull
    private ScooterTypeResponseDTO type;
    @NonNull
    @Column(name = "time_millage")
    private Integer timeMillage;
    @NonNull
    private ScooterStatus status;
}
