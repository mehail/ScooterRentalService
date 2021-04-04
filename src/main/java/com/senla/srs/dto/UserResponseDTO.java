package com.senla.srs.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class UserResponseDTO extends UserDTO{
    @NonNull
    private Long id;
    private List<SeasonTicketDTO> seasonTickets;
}
