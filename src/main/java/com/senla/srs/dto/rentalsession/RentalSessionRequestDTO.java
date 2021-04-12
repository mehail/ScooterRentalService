package com.senla.srs.dto.rentalsession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "scooterSerialNumber"}, callSuper = false)
public class RentalSessionRequestDTO extends RentalSessionDTO {
    @NonNull
    private Long userId;
    @NonNull
    private String scooterSerialNumber;
    private Long seasonTicketId;
    private String promoCodName;
}
