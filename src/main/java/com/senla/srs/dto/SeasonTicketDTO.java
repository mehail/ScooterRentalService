package com.senla.srs.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class SeasonTicketDTO extends AbstractDTO {
    @NonNull
    private Long id;
    @NonNull
    private UserDTO user;
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
