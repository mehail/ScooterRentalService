package com.senla.srs.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"user", "scooter", "begin"}, callSuper = false)
public class RentalSessionRequestDTO extends AbstractDTO {
    @NonNull
    private UserResponseDTO user;
    @NonNull
    private ScooterDTO scooter;
    @NonNull
    private LocalDate begin;
    private LocalDate end;
    private SeasonTicketRequestDTO seasonTicket;
    private PromoCodDTO promoCod;
}
