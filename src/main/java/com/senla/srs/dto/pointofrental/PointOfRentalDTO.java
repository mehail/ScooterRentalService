package com.senla.srs.dto.pointofrental;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class PointOfRentalDTO extends AbstractDTO {
    @NonNull
    private String name;
    @NonNull
    private Boolean available;
}
