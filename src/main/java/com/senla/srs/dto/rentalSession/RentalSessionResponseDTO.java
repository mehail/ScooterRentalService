package com.senla.srs.dto.rentalSession;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionResponseDTO extends RentalSessionRequestDTO {
    @NonNull
    private Long id;
    @NonNull
    private Integer rate;
}
