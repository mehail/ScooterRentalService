package com.senla.srs.dto.rentalsession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "scooterSerialNumber"}, callSuper = false)
public class RentalSessionRequestDTO extends RentalSessionDTO {
    @NonNull
    @Min(1)
    private Long userId;
    @NonNull
    @Length(min = 1, max = 64)
    private String scooterSerialNumber;
    @Min(1)
    private Long seasonTicketId;
    private String promoCodName;
    @NonNull
    private LocalDate beginDate;
    @NonNull
    private LocalTime beginTime;
    private LocalDate endDate;
    private LocalTime endTime;
}
