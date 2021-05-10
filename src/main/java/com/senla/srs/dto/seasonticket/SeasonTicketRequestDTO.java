package com.senla.srs.dto.seasonticket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"price", "scooterTypeId"}, callSuper = false)
public class SeasonTicketRequestDTO extends SeasonTicketDTO {

    @NonNull
    @Min(value = 1, message = "Scooter type ID must be at least 1")
    private Long scooterTypeId;
    @NonNull
    @Min(value = 1, message = "Price must be at least 1")
    private Integer price;

}
