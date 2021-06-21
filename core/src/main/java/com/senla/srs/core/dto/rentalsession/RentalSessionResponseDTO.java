package com.senla.srs.core.dto.rentalsession;

import com.senla.srs.core.dto.promocod.PromoCodDTO;
import com.senla.srs.core.dto.user.UserCompactResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class RentalSessionResponseDTO extends RentalSessionDTO {

    @NonNull
    private Long id;
    @NonNull
    private UserCompactResponseDTO user;
    @NonNull
    private Integer rate;
    private PromoCodDTO promoCod;

}
