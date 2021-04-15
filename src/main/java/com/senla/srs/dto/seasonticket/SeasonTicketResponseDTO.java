package com.senla.srs.dto.seasonticket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class SeasonTicketResponseDTO extends SeasonTicketDTO {
    @NonNull
    private Long id;
    @NonNull
    private Integer remainingTime;
    @NonNull
    private LocalDate expiredDate;
    @NonNull
    private Boolean availableForUse;
}
