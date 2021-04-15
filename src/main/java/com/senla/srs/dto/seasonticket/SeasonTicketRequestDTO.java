package com.senla.srs.dto.seasonticket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"price"}, callSuper = false)
public class SeasonTicketRequestDTO extends SeasonTicketDTO {
    @NonNull
    private Integer price;
}
