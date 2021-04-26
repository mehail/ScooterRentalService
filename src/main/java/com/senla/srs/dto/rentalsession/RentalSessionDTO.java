package com.senla.srs.dto.rentalsession;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"beginDate", "beginTime"}, callSuper = false)
public class RentalSessionDTO extends AbstractDTO {
    @NonNull
    private LocalDate beginDate;
    @NonNull
    private LocalTime beginTime;
    private LocalDate endDate;
    private LocalTime endTime;
}
