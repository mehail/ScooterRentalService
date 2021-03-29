package com.senla.srs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class PointOfRentalDTO extends AbstractDTO {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String city;
    @NonNull
    private String address;
}
