package com.senla.srs.dto.seasonticket;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class SeasonTicketResponseDTO extends SeasonTicketRequestDTO {
    @NonNull
    private Long id;
    @NonNull
    private Integer price;
    @NonNull
    private LocalDate expiredDate;
}
