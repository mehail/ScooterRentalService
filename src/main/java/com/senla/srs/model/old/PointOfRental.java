package com.senla.srs.model.old;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Data
//@Entity
//@Table(name = "point_of_rentals")
public class PointOfRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "cities_id")
    private String city;
    private String address;
}
