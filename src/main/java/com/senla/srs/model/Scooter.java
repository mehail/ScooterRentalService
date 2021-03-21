package com.senla.srs.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
@Table(name = "scooters")
public class Scooter {
    @Id
    @Column(name = "serial_number")
    private String serialNumber;
    @NonNull
    @ManyToOne(optional=false, cascade= CascadeType.ALL)
    @JoinColumn(name = "point_of_rental_id")
    private PointOfRental pointOfRental;
    @NonNull
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "scooter_type_id")
    private ScooterType type;
    @NonNull
    @Column(name = "time_millage")
    private Integer timeMillage;
    @NonNull
    @Enumerated(EnumType.ORDINAL)
    private ScooterStatus status;

    public Scooter() {
    }
}
