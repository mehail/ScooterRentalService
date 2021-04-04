package com.senla.srs.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class PromoCodDTO extends AbstractDTO {
    @NonNull
    private String name;
    private Long rentalSessionId;
    @NonNull
    private LocalDate startDate;
    private LocalDate expiredDate;
    private Integer discountPercentage;
    private Integer bonusPoint;
    private Boolean available;
}
