package com.senla.srs.dto.seasonticket;

import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class SeasonTicketResponseDTO extends SeasonTicketRequestDTO {
    @NonNull
    private Long id;
}
