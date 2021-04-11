package com.senla.srs.dto.user;

import com.senla.srs.dto.seasonticket.SeasonTicketResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class UserFullResponseDTO extends UserDTO {
    @NonNull
    private Long id;
    private List<SeasonTicketResponseDTO> seasonTickets;
}
