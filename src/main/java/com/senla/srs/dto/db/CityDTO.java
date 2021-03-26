package com.senla.srs.dto.db;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "cities")
public class CityDTO implements Serializable {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryDTO country;
}
