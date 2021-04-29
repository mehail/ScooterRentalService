package com.senla.srs.dto.scooter;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.model.ScooterStatus;
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
    @Length(min = 1, max = 64)
    private String serialNumber;
    @NonNull
    @Min(1)
    private Long pointOfRentalId;
    @NonNull
    @Min(1)
    private Integer timeMillage;
    @NonNull
    private ScooterStatus status;
}
