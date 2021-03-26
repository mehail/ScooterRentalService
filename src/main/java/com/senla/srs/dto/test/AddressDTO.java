package com.senla.srs.dto.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class AddressDTO {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityDTO city;
}
