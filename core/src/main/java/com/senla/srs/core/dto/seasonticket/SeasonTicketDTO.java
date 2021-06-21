package com.senla.srs.core.dto.seasonticket;

import com.senla.srs.core.dto.AbstractDTO;
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
    @Min(value = 1, message = "User ID must be at least 1")
    private Long userId;
    @NonNull
    private LocalDate startDate;

}
