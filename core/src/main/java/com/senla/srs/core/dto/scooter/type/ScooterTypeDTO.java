package com.senla.srs.core.dto.scooter.type;

import com.senla.srs.core.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"model"}, callSuper = false)
public class ScooterTypeDTO extends AbstractDTO {

    @NonNull
    @Length(min = 1, max = 64)
    private String model;
    @NonNull
    @Min(1)
    private Double maxSpeed;
    @NonNull
    @Min(1)
    private Integer pricePerMinute;

}
