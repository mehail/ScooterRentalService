package com.senla.srs.dto.rentalsession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "scooterSerialNumber"}, callSuper = false)
public class RentalSessionRequestDTO extends RentalSessionDTO {
    @NonNull
    @Min(value = 1, message = "User ID must be at least 1")
    private Long userId;
    @NonNull
    @Length(min = 1, max = 64, message = "Scooter serial number must be between 1 and 64 characters")
    private String scooterSerialNumber;
    @Min(value = 1, message = "Season ticket ID must be at least 1")
    private Long seasonTicketId;
    private String promoCodName;
}
