package com.senla.srs.dto.scooter;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.entity.ScooterStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"serialNumber"}, callSuper = false)
public class ScooterDTO extends AbstractDTO {

    @NonNull
    @Length(min = 1, max = 64, message = "Serial number must be between 1 and 64 characters")
    private String serialNumber;
    @NonNull
    @Min(value = 1, message = "Point of rental ID must be at least 1")
    private Long pointOfRentalId;
    @NonNull
    @Min(value = 0, message = "Time millage must be positive")
    private Integer timeMillage;
    @NonNull
    private ScooterStatus status;

}
