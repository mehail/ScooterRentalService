package com.senla.srs.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class ScooterTypeDTO extends AbstractDTO {
    @NonNull
    private Long id;
    @NonNull
    private String model;
    @NonNull
    private String maker;
    @NonNull
    private Double maxSpeed;
    @NonNull
    private Integer pricePerMinute;
}
