package com.senla.srs.dto.test;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cities")
public class CityDTO {
    @Id
    private Long id;
    private String city;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryDTO country;
}
