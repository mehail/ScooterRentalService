package com.senla.srs.dto.user;

import com.senla.srs.dto.seasonTicket.SeasonTicketRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class UserResponseDTO extends UserDTO {
    @NonNull
    private Long id;
    private List<SeasonTicketRequestDTO> seasonTickets;
}
