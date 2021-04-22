package com.senla.srs.dto.seasonticket;

import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"scooterType"}, callSuper = false)
public class SeasonTicketFullResponseDTO extends SeasonTicketResponseDTO {
    @NonNull
    private ScooterTypeResponseDTO scooterType;
}
