package com.senla.srs.dto.rentalsession;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.dto.seasonticket.SeasonTicketResponseDTO;
import com.senla.srs.dto.user.UserCompactResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionResponseDTO extends RentalSessionDTO {
    @NonNull
    private Long id;
    @NonNull
    private UserCompactResponseDTO user;
    @NonNull
    private ScooterResponseDTO scooter;
    @NonNull
    private Integer rate;
    private SeasonTicketResponseDTO seasonTicket;
    private PromoCodDTO promoCod;
}
