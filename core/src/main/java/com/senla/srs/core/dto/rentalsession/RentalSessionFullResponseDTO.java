package com.senla.srs.core.dto.rentalsession;

import com.senla.srs.core.dto.promocod.PromoCodDTO;
import com.senla.srs.core.dto.scooter.ScooterCompactResponseDTO;
import com.senla.srs.core.dto.seasonticket.SeasonTicketCompactResponseDTO;
import com.senla.srs.core.dto.user.UserCompactResponseDTO;
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
