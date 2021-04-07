package com.senla.srs.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class SeasonTicketResponseDTO extends SeasonTicketRequestDTO{
    @NonNull
    private Long id;
}
