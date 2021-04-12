package com.senla.srs.dto.rentalsession;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"begin", "end"}, callSuper = false)
public class RentalSessionDTO extends AbstractDTO {
    @NonNull
    private LocalDateTime begin;
    private LocalDateTime end;
}
