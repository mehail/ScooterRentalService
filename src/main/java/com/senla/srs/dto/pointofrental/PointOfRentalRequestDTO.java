package com.senla.srs.dto.pointofrental;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.dto.db.AddressDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "address"}, callSuper = false)
public class PointOfRentalRequestDTO extends AbstractDTO {
    @NonNull
    private String name;
    @NonNull
    private AddressDTO address;
    @NonNull
    private Boolean available;
}
