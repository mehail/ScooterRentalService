package com.senla.srs.dto.scooter.type;

import com.senla.srs.dto.db.MakerDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class ScooterTypeResponseDTO extends ScooterTypeDTO {

    @NonNull
    private Long id;
    @NonNull
    private MakerDTO maker;

}
