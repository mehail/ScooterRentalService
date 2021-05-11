package com.senla.srs.dto.rentalsession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionCompactResponseDTO extends RentalSessionResponseDTO {

    @NonNull
    private String scooterId;

}
