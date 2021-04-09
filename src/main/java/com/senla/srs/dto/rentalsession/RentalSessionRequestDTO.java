package com.senla.srs.dto.rentalsession;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.dto.user.UserResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
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
    //ToDo change
    private SeasonTicketRequestDTO seasonTicket;
    private PromoCodDTO promoCod;
}
