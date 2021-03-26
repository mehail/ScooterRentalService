package com.senla.srs.dto.test;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "countries")
public class CountryDTO {
    @Id
    private Long id;
    private String country;
}
