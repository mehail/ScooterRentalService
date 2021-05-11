package com.senla.srs.dto.rentalsession;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.dto.scooter.ScooterCompactResponseDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketCompactResponseDTO;
import com.senla.srs.dto.user.UserCompactResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionFullResponseDTO extends RentalSessionResponseDTO {

    @NonNull
    private Long id;
    @NonNull
    private UserCompactResponseDTO user;
    @NonNull
    private ScooterCompactResponseDTO scooter;
    @NonNull
    private Integer rate;
    private SeasonTicketCompactResponseDTO seasonTicket;
    private PromoCodDTO promoCod;

}
