package com.senla.srs.dto.pointofrental;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class PointOfRentalDTO extends AbstractDTO {
    @NonNull
    @Length(min = 1, max = 64, message = "Name must be between 1 and 64 characters")
    private String name;
    @NonNull
    private Boolean available;
}
