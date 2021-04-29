package com.senla.srs.dto.seasonticket;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "startDate"}, callSuper = false)
public class SeasonTicketDTO extends AbstractDTO {
    @NonNull
    @Min(1)
    private Long userId;
    @NonNull
    private LocalDate startDate;
}
