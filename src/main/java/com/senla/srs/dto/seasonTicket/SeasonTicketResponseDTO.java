package com.senla.srs.dto.seasonTicket;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class SeasonTicketResponseDTO extends SeasonTicketRequestDTO {
    @NonNull
    private Long id;
}
