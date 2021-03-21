package com.senla.srs.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
@Table(name = "point_of_rentals")
public class PointOfRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @Column(name = "cities_id")
    private String city;
    @NonNull
    private String address;
    @NonNull
    private Boolean available;

    public PointOfRental() {
    }
}
