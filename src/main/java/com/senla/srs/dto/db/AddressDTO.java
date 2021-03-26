package com.senla.srs.dto.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class AddressDTO implements Serializable {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityDTO city;
}
