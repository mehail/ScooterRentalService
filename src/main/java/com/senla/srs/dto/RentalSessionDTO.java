package com.senla.srs.dto;

import com.senla.srs.model.Scooter;
import com.senla.srs.model.User;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionDTO extends AbstractDTO {
    @NonNull
    private Long id;
    @NonNull
    private User user;
    @NonNull
    private Scooter scooter;
    @NonNull
    private Integer rate;
    @NonNull
    private LocalDate begin;
    private LocalDate end;
    private SeasonTicketDTO seasonTicket;
    private List<PromoCodDTO> promoCods;
}
