package com.senla.srs.dto.rentalsession;

import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionResponseDTO extends RentalSessionRequestDTO {
    @NonNull
    private Long id;
    @NonNull
    private Integer rate;
}
