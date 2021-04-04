package com.senla.srs.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionDTO extends AbstractDTO {
    @NonNull
    private Long id;
    @NonNull
    private UserResponseDTO user;
    @NonNull
    private ScooterDTO scooter;
    @NonNull
    private Integer rate;
    @NonNull
    private LocalDate begin;
    private LocalDate end;
    private SeasonTicketDTO seasonTicket;
    private List<PromoCodDTO> promoCods;
}
