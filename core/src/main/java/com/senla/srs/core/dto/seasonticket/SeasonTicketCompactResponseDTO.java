package com.senla.srs.core.dto.seasonticket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"scooterTypeId"}, callSuper = false)
public class SeasonTicketCompactResponseDTO extends SeasonTicketResponseDTO {

    @NonNull
    private Long scooterTypeId;

}
