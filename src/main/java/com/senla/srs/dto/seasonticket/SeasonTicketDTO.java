package com.senla.srs.dto.seasonticket;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "remainingTime", "startDate"}, callSuper = false)
public class SeasonTicketDTO extends AbstractDTO {
    @NonNull
    private Long userId;
    @NonNull
    private Integer remainingTime;
    @NonNull
    private LocalDate startDate;
}
