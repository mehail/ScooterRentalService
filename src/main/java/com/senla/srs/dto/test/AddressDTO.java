package com.senla.srs.dto.test;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String name;
    private CityDTO city;
}
