package com.senla.srs.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "scooterType", "startDate"}, callSuper = false)
public class SeasonTicketRequestDTO extends AbstractDTO {
    @NonNull
    private Long userId;
    @NonNull
    private ScooterTypeDTO scooterType;
    @NonNull
    private Integer price;
    @NonNull
    private Integer remainingTime;
    private LocalDate startDate;
    private LocalDate expiredDate;
    @NonNull
    private Boolean availableForUse;
}
