package com.senla.srs.dto.pointofrental;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"addressId"}, callSuper = false)
public class PointOfRentalRequestDTO extends PointOfRentalDTO {

    @NonNull
    @Min(value = 1, message = "Address ID must be at least 1")
    private Long addressId;

}
