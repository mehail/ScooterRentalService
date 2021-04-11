package com.senla.srs.dto.seasonticket;

import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class SeasonTicketResponseDTO extends SeasonTicketDTO {
    @NonNull
    private Long id;
    @NonNull
    private ScooterTypeResponseDTO scooterType;
    @NonNull
    private Integer price;
    @NonNull
    private LocalDate expiredDate;
    @NonNull
    private Boolean availableForUse;
}
