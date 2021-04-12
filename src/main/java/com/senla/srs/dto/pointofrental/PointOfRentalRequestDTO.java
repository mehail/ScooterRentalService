package com.senla.srs.dto.pointofrental;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"addressId"}, callSuper = false)
public class PointOfRentalRequestDTO extends PointOfRentalDTO {
    @NonNull
    private Long addressId;
}
