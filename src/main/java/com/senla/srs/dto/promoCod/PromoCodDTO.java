package com.senla.srs.dto.promoCod;

import com.senla.srs.dto.AbstractDTO;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class PromoCodDTO extends AbstractDTO {
    @NonNull
    private String name;
    @NonNull
    private LocalDate startDate;
    private LocalDate expiredDate;
    private Integer discountPercentage;
    private Integer bonusPoint;
    private Boolean available;
}
