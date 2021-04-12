package com.senla.srs.dto.scooter.type;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"model"}, callSuper = false)
public class ScooterTypeDTO extends AbstractDTO {
    @NonNull
    private String model;
    @NonNull
    private Double maxSpeed;
    @NonNull
    private Integer pricePerMinute;
}
