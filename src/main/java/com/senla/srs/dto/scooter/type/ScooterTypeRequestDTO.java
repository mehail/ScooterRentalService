package com.senla.srs.dto.scooter.type;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.dto.db.MakerDTO;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"model", "maker"}, callSuper = false)
public class ScooterTypeRequestDTO extends AbstractDTO {
    @NonNull
    private String model;
    @NonNull
    private MakerDTO maker;
    @NonNull
    private Double maxSpeed;
    @NonNull
    private Integer pricePerMinute;
}
