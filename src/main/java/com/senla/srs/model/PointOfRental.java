package com.senla.srs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "point_of_rentals")
public class PointOfRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "cities_id")
    private String city;
    private String address;
    private Boolean available;
}
